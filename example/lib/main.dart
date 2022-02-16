import 'dart:developer';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:btc_sign/btc_sign.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    // initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  // Future<void> initPlatformState() async {
  //   String platformVersion;
  //   // Platform messages may fail, so we use a try/catch PlatformException.
  //   // We also handle the message potentially returning null.
  //   try {
  //     platformVersion =
  //         await BtcSign.platformVersion ?? 'Unknown platform version';
  //   } on PlatformException {
  //     platformVersion = 'Failed to get platform version.';
  //   }
  //
  //   // If the widget was removed from the tree while the asynchronous platform
  //   // message was in flight, we want to discard the reply rather than calling
  //   // setState to update our non-existent appearance.
  //   if (!mounted) return;
  //
  //   setState(() {
  //     _platformVersion = platformVersion;
  //   });
  // }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {
              var size = await BtcSign.sign([
                {
                  "tx_hash_big_endian": "a5c8c86386ac2150e416ffa491460234e51100929b905936b5fb8134c8de40a9",
                  "tx_output_n": 0,
                  "script": "76a914fa188e595139846eb4d809dac8bfc206227f404388ac",
                  "value": 20000
                },
                {
                  "tx_hash_big_endian": "a5c8c86386ac2150e416ffa491460234e51100929b905936b5fb8134c8de40a9",
                  "tx_output_n": 0,
                  "script": "76a914fa188e595139846eb4d809dac8bfc206227f404388ac",
                  "value": 20000
                },
                {
                  "tx_hash_big_endian": "a5c8c86386ac2150e416ffa491460234e51100929b905936b5fb8134c8de40a9",
                  "tx_output_n": 0,
                  "script": "76a914fa188e595139846eb4d809dac8bfc206227f404388ac",
                  "value": 20000
                },
                {
                  "tx_hash_big_endian": "a5c8c86386ac2150e416ffa491460234e51100929b905936b5fb8134c8de40a9",
                  "tx_output_n": 0,
                  "script": "76a914fa188e595139846eb4d809dac8bfc206227f404388ac",
                  "value": 20000
                }
              ]);
              log("Size $size");
            },
            child: Text("HELLO"),
          ),
        ),
      ),
    );
  }
}
