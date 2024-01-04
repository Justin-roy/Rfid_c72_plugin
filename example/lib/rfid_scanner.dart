import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:rfid_c72_plugin/rfid_c72_plugin.dart';
import 'package:rfid_c72_plugin/tag_epc.dart';

class RfidScanner extends StatefulWidget {
  const RfidScanner({Key? key}) : super(key: key);

  @override
  State<RfidScanner> createState() => _RfidScannerState();
}

class _RfidScannerState extends State<RfidScanner> {
  String _platformVersion = 'Unknown';
  final bool _isHaveSavedData = false;
  final bool _isStarted = false;
  final bool _isEmptyTags = false;
  bool _isConnected = false;
  bool _isLoading = true;
  int _totalEPC = 0, _invalidEPC = 0, _scannedEPC = 0;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  @override
  void dispose() {
    super.dispose();
    closeAll();
  }

  //Hopefully we free memory in the device.
  closeAll() {
    RfidC72Plugin.stopScan;
    RfidC72Plugin.close;
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = (await RfidC72Plugin.platformVersion)!;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    RfidC72Plugin.connectedStatusStream
        .receiveBroadcastStream()
        .listen(updateIsConnected);
    RfidC72Plugin.tagsStatusStream.receiveBroadcastStream().listen(updateTags);
    await RfidC72Plugin.connect;
    // await UhfC72Plugin.setWorkArea('2');
    // await UhfC72Plugin.setPowerLevel('30');
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.

    await RfidC72Plugin.connectBarcode; //connect barcode
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _isLoading = false;
    });
  }

  List<TagEpc> _data = [];
  final List<String> _EPC = [];

  void updateTags(dynamic result) async {
    setState(() {
      _data = TagEpc.parseTags(result);
      _totalEPC = _data.toSet().toList().length;
    });
  }

  List<String?> _barcodes = [];

  void updateIsConnected(dynamic isConnected) {
    //setState(() {
    _isConnected = isConnected;
    //});
  }

  bool _isContinuousCall = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Rfid and 2D Barcode Reader C72'),
      ),
      body: SingleChildScrollView(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            const Card(
              child: Padding(
                padding: EdgeInsets.all(3.0),
                child: Icon(
                  Icons.adf_scanner_outlined,
                  size: 100,
                ),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.green,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(29.0),
                      ),
                    ),
                    child: const Text(
                      'Start Single Reading',
                      style: TextStyle(color: Colors.white),
                    ),
                    onPressed: () async {
                      await RfidC72Plugin.startSingle;
                    }),
                ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor:
                      _isContinuousCall ? Colors.red : Colors.green,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(29.0),
                      ),
                    ),
                    child: _isContinuousCall
                        ? const Text(
                      'Stop Continuous Reading',
                      style: TextStyle(color: Colors.white),
                    )
                        : const Text(
                      'Start Continuous Reading',
                      style: TextStyle(color: Colors.white),
                    ),
                    onPressed: () async {
                      bool? isStarted = _isContinuousCall == true
                          ? await RfidC72Plugin.stop
                          : await RfidC72Plugin.startContinuous;
                      setState(() {
                        _isContinuousCall = !_isContinuousCall;
                      });
                    }),
              ],
            ),
            ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blueAccent,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(29.0),
                  ),
                ),
                child: const Text(
                  'Clear Data',
                  style: TextStyle(color: Colors.white),
                ),
                onPressed: () async {
                  await RfidC72Plugin.clearData;
                  setState(() {
                    _data.clear();
                    //     _logs.clear();
                  });
                }),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.redAccent,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(29.0),
                      ),
                    ),
                    child: const Text(
                      'Start 2D Barcode scan',
                      style: TextStyle(color: Colors.white),
                    ),
                    onPressed: () async {
                      await RfidC72Plugin.scanBarcode;
                      await Future.delayed(const Duration(seconds: 1));
                      String? scannedCode = await RfidC72Plugin.readBarcode;
                      setState(() {
                        _barcodes.add(scannedCode);
                      });
                    }),
                ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.redAccent,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(29.0),
                      ),
                    ),
                    child: const Text(
                      'Stop 2D Barcode scan',
                      style: TextStyle(color: Colors.white),
                    ),
                    onPressed: () async {
                      await RfidC72Plugin.stopScan;
                      await Future.delayed(const Duration(seconds: 1));
                      setState(() {
                        _barcodes = [];
                      });
                    }),
              ],
            ),
            Container(
              width: MediaQuery.of(context).size.width,
              height: 50,
              color: Colors.blue[400],
              child: Center(
                child: Text(
                  'Barcodes: $_barcodes',
                  style: GoogleFonts.lato(
                    color: Colors.black,
                    fontSize: 22,
                  ),
                ),
              ),
            ),
            Container(
              width: MediaQuery.of(context).size.width,
              height: 50,
              color: Colors.blue[400],
              child: Center(
                child: Text(
                  'Total EPC: $_totalEPC',
                  style: GoogleFonts.lato(
                    color: Colors.black,
                    fontSize: 22,
                  ),
                ),
              ),
            ),
            ..._data.map(
                  (TagEpc tag) {
                _EPC.add(tag.epc.replaceAll(RegExp('EPC:'), ''));
                return Card(
                  color: Colors.blue.shade50,
                  child: Container(
                    width: 330,
                    alignment: Alignment.center,
                    padding: const EdgeInsets.all(8.0),
                    child: Text(
                      'Tag: ${tag.epc.replaceAll(RegExp('EPC:'), '')}',
                      style: TextStyle(color: Colors.blue.shade800),
                    ),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
