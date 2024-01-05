import 'dart:async';

import 'package:flutter/services.dart';

class RfidC72Plugin {
  static const MethodChannel _channel = MethodChannel('rfid_c72_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static const EventChannel connectedStatusStream =
  EventChannel('ConnectedStatus');
  static const EventChannel tagsStatusStream = EventChannel('TagsStatus');

  static Future<bool?> get isStarted async {
    return _channel.invokeMethod('isStarted');
  }

  static Future<bool?> get startSingle async {
    return _channel.invokeMethod('startSingle');
  }

  static Future<bool?> get startContinuous async {
    return _channel.invokeMethod('startContinuous');
  }

  static Future<bool?> get stop async {
    return _channel.invokeMethod('stop');
  }

  static Future<bool?> get close async {
    return _channel.invokeMethod('close');
  }

  static Future<bool?> get clearData async {
    return _channel.invokeMethod('clearData');
  }

  static Future<bool?> get isEmptyTags async {
    return _channel.invokeMethod('isEmptyTags');
  }

  static Future<bool?> get connect async {
    return _channel.invokeMethod('connect');
  }

  static Future<bool?> get isConnected async {
    return _channel.invokeMethod('isConnected');
  }

  static Future<bool?> get connectBarcode async {
    return _channel.invokeMethod('connectBarcode');
  }

  static Future<bool?> get scanBarcode async {
    return _channel.invokeMethod('scanBarcode');
  }

  static Future<bool?> get stopScan async {
    return _channel.invokeMethod('stopScan');
  }


  static Future<bool?> get closeScan async {
    return _channel.invokeMethod('closeScan');
  }

  static Future<bool?> setPowerLevel(String value) async {
    return _channel
        .invokeMethod('setPowerLevel', <String, String>{'value': value});
  }

  static Future<bool?> setWorkArea(String value) async {
    return _channel
        .invokeMethod('setWorkArea', <String, String>{'value': value});
  }

  static Future<String?> get readBarcode async {
    final String? barcode = await _channel.invokeMethod('readBarcode');
    return barcode;
  }
}
