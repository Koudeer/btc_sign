import 'dart:async';

import 'package:flutter/services.dart';

class BtcSign {
  static const MethodChannel _channel = MethodChannel('btc_sign');

  static Future<String?> sign(
      {required String from,
      required String to,
      required num amount,
      required num fee,
      required String privateKey,
      required List<Map<String, dynamic>> utxos}) async {

    if(utxos.isEmpty){
      throw ArgumentError("Utxos is Empty");
    }

    var tx = await _channel.invokeMethod("sign", <String, dynamic>{
      "from": from,
      "to": to,
      "amount": amount,
      "fee": fee,
      "privateKey": privateKey,
      "utxos": utxos,
    });
    return tx;
  }
}
