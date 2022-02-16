import 'dart:async';

import 'package:flutter/services.dart';

class BtcSign {
  static const MethodChannel _channel = MethodChannel('btc_sign');

  // static Future<String?> get platformVersion async {
  //   final String? version = await _channel.invokeMethod('getPlatformVersion');
  //   return version;
  // }

  static Future<int?> sign(List<Map<String, dynamic>> utxos) async {
    var tx =
        await _channel.invokeMethod("sign", <String, dynamic>{"utxos": utxos});
    return tx;
  }

  static Future<String?> hello(String word) async{
    return await _channel.invokeMethod("sayword",word);
  }
}
