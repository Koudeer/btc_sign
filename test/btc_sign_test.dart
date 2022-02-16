import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:btc_sign/btc_sign.dart';

void main() {
  const MethodChannel channel = MethodChannel('btc_sign');

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
    expect(await BtcSign.platformVersion, '42');
  });
}
