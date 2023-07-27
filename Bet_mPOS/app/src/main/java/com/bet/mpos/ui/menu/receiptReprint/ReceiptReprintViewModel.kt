package com.bet.mpos.ui.menu.receiptReprint

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.util.ESharedPreferences
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.collection.ReceiptType
import com.zoop.sdk.api.terminal.Printer
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSPrinterResponse

class ReceiptReprintViewModel: ViewModel() {

    private var mActivity: FragmentActivity? = null

    private val _error = MutableLiveData<String>().apply {}

    var error: LiveData<String> = _error

    fun reprint(s: String) {
        if(s == "client"){
            printReceipt(ReceiptType.REPRINT_CUSTOMER)
        }else{
            printReceipt(ReceiptType.REPRINT_ESTABLISHMENT)
        }
    }

    private fun printReceipt(type: ReceiptType) {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json: String? = esp.getString(BetApp.getAppContext().getString(R.string.saved_transaction_data_file_name), "")
            val obj: com.zoop.sdk.api.collection.TransactionData =
                gson.fromJson(json, com.zoop.sdk.api.collection.TransactionData::class.java)

            val transactionData = obj
            val request = SmartPOSPlugin.createPrintRequestBuilder()
                .printData(Printer.PrintData(transactionData = transactionData))
                .receiptType(type)
                .callback(object : Callback<SmartPOSPrinterResponse>() {
                    override fun onStart() {
                        handlePrintStarted()
                    }

                    override fun onSuccess(response: SmartPOSPrinterResponse) {
                        handlePrintSuccess()
                    }

                    override fun onFail(error: Throwable) {
                        handlePrinterError(error.message.toString())
                    }

                    override fun onComplete() {
                        handlePrintFinished()
                    }

                }).build()
            Zoop.post(request)
        }catch (e: Exception) {
            Log.e("Reprint: ", e.toString())
            handlePrinterError("Não ha transação")
        }

    }

    private fun handlePrintFinished() {

    }

    private fun handlePrinterError(message: String) {
        mActivity?.runOnUiThread {
            Toast.makeText(BetApp.getAppContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePrintSuccess() {

    }

    private fun handlePrintStarted() {

    }

    fun start(requireActivity: FragmentActivity) {
        mActivity = requireActivity
    }
}