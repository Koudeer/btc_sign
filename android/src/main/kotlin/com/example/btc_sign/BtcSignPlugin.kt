package com.example.btc_sign

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.bitcoinj.*
import org.bitcoinj.core.Coin
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.UTXO
import org.bitcoinj.script.Script
import org.bouncycastle.util.encoders.Hex

/** BtcSignPlugin */
class BtcSignPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "btc_sign")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "sign" -> {
                val datas : List<Map<String,Any>>? = call.argument("utxos")
                val list = handleUtxo(datas)
                result.success(list.size)
            }
            "sayword"->{
                val passphrase: String = call.arguments()
                result.success(passphrase ?:"NULL")
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }


    private fun handleUtxo(datas: List<Map<String, Any>>?): List<UTXO> {
        val utxos = mutableListOf<UTXO>()

        if (datas?.isNotEmpty() == true) {
            utxos.clear()
            datas.forEach {
                val tx_hash_big_endian = it["tx_hash_big_endian"] as String
                val tx_output_n = (it["tx_output_n"] as Int).toLong()
                val script = it["script"] as String
                val value = (it["value"] as Int).toLong()
                utxos.add(
                    UTXO(
                        Sha256Hash.wrap(tx_hash_big_endian),
                        tx_output_n,
                        Coin.valueOf(value),
                        0,
                        false,
                        Script(Hex.decode(script))
                    )
                )
            }
        }

        return utxos
    }
}
