package com.bet.mpos.ui.payment.transactionPix

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.dialogs.CustomersCopyDialog
import com.bet.mpos.util.Functions
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.Request
import com.zoop.sdk.api.collection.ReceiptType
import com.zoop.sdk.api.collection.TransactionData
import com.zoop.sdk.api.requestfield.MessageCallbackRequestField
import com.zoop.sdk.api.requestfield.QRCodeCallbackRequestField
import com.zoop.sdk.api.terminal.Printer
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSPixPaymentResponse
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSPrinterResponse

class TransactionPixViewModel: ViewModel() {
    private var total = 0;
    private var extra = ""
    private var typeSale = ""
    private var installments = 1
    private var mActivity: FragmentActivity? = null
    private var pixRequest: Request? = null

    private var navController: NavController? = null
    private var dialog : CustomersCopyDialog? = null

    private val _value = MutableLiveData<String>().apply {}
    private val _userMessage = MutableLiveData<String>().apply {}
    private val _qrCodeImage = MutableLiveData<Bitmap>().apply {}
    private val _loading = MutableLiveData<Boolean>().apply {}

    var value: LiveData<String> = _value
    val userMessage: LiveData<String> = _userMessage
    val qrCodeImage: LiveData<Bitmap> = _qrCodeImage
    val loading: LiveData<Boolean> = _loading

    fun start(
        arguments: Bundle?,
        findNavController: NavController,
        requireActivity: FragmentActivity
    ) {
        navController = findNavController
        mActivity = requireActivity
        if(loadArguments(arguments)) {
            initializePayment()
            _loading.value = true
        }
    }

    fun cancelClick() {
        val bundle = Bundle()

        bundle.putInt("value", total)
        bundle.putString("typeSale", typeSale)
        bundle.putString("extra", extra)

        bundle.putBoolean("error", true)
        bundle.putString("errorMsg", "Transação cancelada")

        _userMessage.postValue("CANCELANDO...")
        pixRequest?.cancel()
        navController?.navigate(
            R.id.action_transactionPixFragment_to_transactionFailureFragment,
            bundle
        )
    }

    fun loadArguments(arguments: Bundle?): Boolean {
        if(arguments != null) {
            total = arguments.getInt("value")
            extra = arguments.getString("extra", "")
            typeSale = arguments.getString("typeSale", "")
            installments = arguments.getInt("installments", 1)

            _value.value = Functions.int_to_real(total)
            return true
        }
        else{
            val bundle = Bundle()
            bundle.putBoolean("error", true)
            bundle.putString("errorMsg", BetApp.getAppContext().getString(R.string.error_generic))
            mActivity?.runOnUiThread {
                navController?.navigate(R.id.action_transactionFragment_to_transactionFailureFragment)
            }
            return false
        }
    }

    fun initializePayment(){
        println("Initializing pix")
        pixRequest = SmartPOSPlugin.createPixPaymentRequestBuilder()
            .amount(total.toLong())
            .callback(object: Callback<SmartPOSPixPaymentResponse>(){
                override fun onSuccess(response: SmartPOSPixPaymentResponse) {
                    handleSuccessfullPayment(response)
                }

                override fun onFail(throwable: Throwable) {
                    //handlePaymentFailure(throwable)

                }
            })
            .qrCodeCallback(object: Callback<QRCodeCallbackRequestField.QRCodeData>() {
                override fun onSuccess(qrCodeData: QRCodeCallbackRequestField.QRCodeData) {
                    _loading.postValue(false)
                    showQRCode(qrCodeData.data)
                }

                override fun onFail(throwable: Throwable) {
                    _loading.postValue(false)
                    handleQrCodeFailure(throwable)
                }
            })
            .messageCallback(object: Callback<MessageCallbackRequestField.MessageData>() {
                override fun onSuccess(messageData: MessageCallbackRequestField.MessageData) {
                    displayMessage(messageData)
                }

                override fun onFail(throwable: Throwable) {
                    Log.e("messageCallbackPix ", "onFail $throwable")
                }
            }).build()
        Zoop.post(pixRequest!!)
    }

    private fun showQRCode(data: String) {

        val bitmap = Functions.generateQRCode(data)
        if(bitmap != null)
            _qrCodeImage.postValue(bitmap)
        else
            _userMessage.postValue("falha ao gerar QRCode")
    }

    private fun handlePaymentFailure(throwable: Throwable) {
        try{
            val bundle = Bundle()

            bundle.putInt("value", total)
            bundle.putString("typeSale", typeSale)
            bundle.putString("extra", extra)

            bundle.putBoolean("error", true)
            bundle.putString("errorMsg", throwable.message)

            mActivity?.runOnUiThread {
                navController?.navigate(
                    R.id.action_transactionPixFragment_to_transactionFailureFragment,
                    bundle
                )
            }

        }catch (e: Exception){
            Log.e("handleSuccessfullPayment", e.toString())
        }
    }

    private fun handleQrCodeFailure(throwable: Throwable) {
        _userMessage.postValue(throwable.message)
    }

    private fun displayMessage(messageData: MessageCallbackRequestField.MessageData) {
        _userMessage.postValue(messageData.message)
    }

    private fun handleSuccessfullPayment(response: SmartPOSPixPaymentResponse) {

        try {
            mActivity?.runOnUiThread {
                printReceipt(response.transactionData, ReceiptType.ESTABLISHMENT)

                dialog = CustomersCopyDialog(mActivity!!, response.transactionData)
                //dialog!!.create()
                dialog!!.show()

                dialog!!.setOnPrintClickListener{
                    printReceipt(response.transactionData, ReceiptType.CUSTOMER)
                }

                dialog!!.setOnCancelClickListener {
                    handlePrintFinished(response.transactionData)
                }
            }
        }catch (e: Exception){
            Log.e("Transaction: ", e.toString())
        }
    }

    private fun printReceipt(transactionData: TransactionData, type: ReceiptType) {
        val transactionData = transactionData
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
                    handlePrinterError()
                }

                override fun onComplete() {
                    if(type == ReceiptType.CUSTOMER)
                        handlePrintFinished(transactionData)
                }

            }).build()
        Zoop.post(request)

    }

    private fun handlePrintFinished(transactionData: TransactionData) {
        val gson = Gson()
        val json = gson.toJson(transactionData)
        val bundle = Bundle()
        bundle.putString("transaction_data", json)
        mActivity?.runOnUiThread {
            dialog?.hide()
            navController?.navigate(
                R.id.action_transactionPixFragment_to_transactionCompletedFragment,
                bundle
            )
        }
    }

    private fun handlePrinterError() {

    }

    private fun handlePrintSuccess() {

    }

    private fun handlePrintStarted() {

    }
}