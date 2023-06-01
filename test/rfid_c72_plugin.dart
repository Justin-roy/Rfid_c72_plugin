import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:rfid_c72_plugin/rfid_c72_plugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('rfid_c72_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await RfidC72Plugin.platformVersion, '42');
  });
}
