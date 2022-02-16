package com.example.btc_sign

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.bitcoinj.*
import org.bitcoinj.core.*
import org.bitcoinj.params.MainNetParams
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
                val tx = btcSign(call)
                result.success(tx)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun btcSign(call: MethodCall): String {
        val datas = call.argument("utxos") as List<Map<String, Any>>?
        val from = call.argument("from") as String?
        val to = call.argument("to") as String?
        val amount = (call.argument("amount") as Int?)?.toLong()
        val fee = (call.argument("fee") as Int?)?.toLong()
        val privateKey = call.argument("privateKey") as String?
        val utxos = handleUtxo(datas)


        val changeAddress = from

        ///开始进行签名

        val networkParameters = MainNetParams.get()
        val transaction = Transaction(networkParameters)

        var changeAmount = 0L
        var utxoAmount = 0L

        val needUtxos = mutableListOf<UTXO>()

        for (utxo in utxos) {
            if (utxoAmount >= (amount!! + fee!!)) {
                break;
            } else {
                needUtxos.add(utxo)
                utxoAmount = utxo.value.value
            }
        }

        transaction.addOutput(
            Coin.valueOf(amount!!),
            Address.fromString(networkParameters, to)
        )

        changeAmount = utxoAmount - (amount!! + fee!!)

        if (changeAmount < 0) {
            return "";
        }

        if (changeAmount > 0) {
            transaction.addOutput(
                Coin.valueOf(changeAmount),
                Address.fromString(networkParameters, changeAddress)
            )
        }

        val dumpedPrivateKey = DumpedPrivateKey.fromBase58(networkParameters, privateKey)
        val ecKey = dumpedPrivateKey.key

        for (needUtxo in needUtxos) {
            val outPoint = TransactionOutPoint(networkParameters, needUtxo.index, needUtxo.hash)
            transaction.addSignedInput(
                outPoint,
                needUtxo.script,
                ecKey,
                Transaction.SigHash.ALL,
                true
            )
        }

        val bytes = transaction.bitcoinSerialize()
        val h = Hex.toHexString(transaction.bitcoinSerialize())

        return h
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
