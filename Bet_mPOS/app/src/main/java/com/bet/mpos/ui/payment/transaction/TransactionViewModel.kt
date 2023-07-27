package com.bet.mpos.ui.payment.transaction

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.util.Functions
import com.bet.mpos.dialogs.CustomersCopyDialog
import com.bet.mpos.util.GenerateBitmap
import com.bet.mpos.util.pax.PrinterTester
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.Option
import com.zoop.sdk.api.Request
import com.zoop.sdk.api.collection.ReceiptType
import com.zoop.sdk.api.collection.TransactionData
import com.zoop.sdk.api.requestfield.CardDetectionTypeRequestField
import com.zoop.sdk.api.requestfield.MessageCallbackRequestField
import com.zoop.sdk.api.requestfield.PinCallbackRequestField
import com.zoop.sdk.api.terminal.Printer
import com.zoop.sdk.api.terminal.Terminal
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSCardDetectionResponse
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSMenuOptions
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSPaymentResponse
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSPrinterResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel: ViewModel() {

    private var total = 0;
    private var extra = ""
    private var typeSale = ""
    private var installments = 1
    private var inputPassword = ""
    private var mActivity: FragmentActivity? = null
    private var paymentRequest: Request? = null
    private var failedPayment = false

    private var navController: NavController? = null

    private var dialog : CustomersCopyDialog? = null

    private val _value = MutableLiveData<String>().apply {}
    private val _animation = MutableLiveData<Boolean>().apply {}
    private val _userMessage = MutableLiveData<String>().apply {}

    var value: LiveData<String> = _value
    var animation: LiveData<Boolean> = _animation
    val userMessage: LiveData<String> = _userMessage

    fun start(
        arguments: Bundle?,
        findNavController: NavController,
        requireActivity: FragmentActivity
    ) {
        navController = findNavController
        mActivity = requireActivity
        if(loadArguments(arguments)) {
            when (typeSale) {
                "only" -> initializePayment(Option.CREDIT)
                "debit" -> initializePayment(Option.DEBIT)
                "seller" -> initializePayment(Option.CREDIT_WITH_INSTALLMENTS)
                "buyer" -> initializePayment(Option.CREDIT_WITH_INSTALLMENTS)
//                "pix" -> initializePayment(Option.PIX)
                else -> {
                    val bundle = Bundle()
                    bundle.putBoolean("error", true)
                    bundle.putString("errorMsg", BetApp.getAppContext().getString(R.string.error_generic))

                    navController!!.navigate(
                        R.id.action_transactionFragment_to_transactionFailureFragment,
                        bundle
                    )
                }
            }
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
        paymentRequest?.cancel()
    }

    private fun loadArguments(arguments: Bundle?): Boolean {
        if(arguments != null) {
            total = arguments.getInt("value")
            extra = arguments.getString("extra", "")
            typeSale = arguments.getString("typeSale", "")
            installments = arguments.getInt("installments", 1)

//            _value.postValue(Functions.int_to_real(total))
            _value.value = Functions.int_to_real(total)
            return true
        }
        else{
            val bundle = Bundle()
            bundle.putBoolean("error", true)
            bundle.putString("errorMsg", BetApp.getAppContext().getString(R.string.error_generic))
            navController?.navigate(R.id.action_transactionFragment_to_transactionFailureFragment)
            return false
        }
    }

    private fun printEstablishmentReceipt(response: SmartPOSPaymentResponse)
    {
        //val bitmap = GenerateBitmap.drawEstablishmentReceipt(response, typeSale)
        val bitmap = GenerateBitmap.drawSaleReceipt(response, typeSale, "estabelecimento")
        Thread {
            val printer = PrinterTester.getInstance()
            printer.init()
            printer.printBitmap(bitmap)
            printer.step(60)
            var res = printer.start()
            if (!res.first)
                Toast.makeText(BetApp.getAppContext(), res.second, Toast.LENGTH_SHORT).show()
        }.start()
    }

    private fun printCustomerReceipt(response: SmartPOSPaymentResponse)
    {
        val bitmap = GenerateBitmap.drawSaleReceipt(response, typeSale, "customer")
        Thread {
            val printer = PrinterTester.getInstance()
            printer.init()
            printer.printBitmap(bitmap)
            printer.step(60)
            var res = printer.start()
            if (!res.first)
                Toast.makeText(BetApp.getAppContext(), res.second, Toast.LENGTH_SHORT).show()
        }.start()
    }

    private fun initializePayment(option: Option){
        println("Initializing payment")

        try {
            paymentRequest = SmartPOSPlugin.createPaymentRequestBuilder()
                .amount(total.toLong())
                .option(option)
                .installments(installments)
                .autoPrintEstablishmentReceipt(false)
                .callback(object : Callback<SmartPOSPaymentResponse>() {
                    override fun onStart() {
                        startLoadingAnimation()
                    }

                    override fun onSuccess(response: SmartPOSPaymentResponse) {
                        Log.e("onSuccess", response.toString())
                        handleSucessfullPayment(response)
                        printEstablishmentReceipt(response)
                    }

                    override fun onFail(exception: Throwable) {
                        handlePaymentFailure(exception)
                    }

                    override fun onComplete() {
                        stopLoadingAnimation()
                    }
                })
                .messageCallback(object : Callback<MessageCallbackRequestField.MessageData>() {
                    override fun onSuccess(messageData: MessageCallbackRequestField.MessageData) {
                        displayUserMessage(messageData.message)
                    }

                    override fun onFail(exception: Throwable) {
                        Log.e("messageCallback onFail ", exception.toString())
                    }
                })
                .pinCallback(object : Callback<PinCallbackRequestField.PinData>() {
                    override fun onSuccess(pinData: PinCallbackRequestField.PinData) {
                        val eventType = pinData.type
                        when (eventType) {
                            Terminal.PinEventHandler.EventType.Start -> creatViewToDisplayPasswordInput()
                            Terminal.PinEventHandler.EventType.Finish -> finishPasswordInput()
                            Terminal.PinEventHandler.EventType.Inserted -> handlePasswordCaracterInput()
                            Terminal.PinEventHandler.EventType.Removed -> handlePasswordCaracterRemoved()
                            else -> handlePasswordCaracterCleared()

                        }
                    }

                    override fun onFail(exception: Throwable) {
                        Log.e("pinCallback onFail ", exception.toString())
                    }
                })
                .menuSelectionCallback(object : Callback<SmartPOSMenuOptions>() {
                    override fun onFail(error: Throwable) {
                        Log.e("menuSelectionCallback ", "onFail $error")
                    }

                    override fun onSuccess(response: SmartPOSMenuOptions) {
                        assembleOptionsList(response)
                    }
                })
                .build()
            Zoop.post(paymentRequest!!)

        }catch (e: Exception) {
            Log.e("paymentRequest", e.toString())
        }
    }

    private fun assembleOptionsList(response: SmartPOSMenuOptions) {
        println("assembleOptionsList")
        println(response.options.iterable)
    }

    private fun handlePasswordCaracterCleared() {
        println("handlePasswordCaracterCleared")
        inputPassword = ""
        _userMessage.postValue("senha: ")
    }

    private fun handlePasswordCaracterRemoved() {
        println("handlePasswordCaracterRemoved")

        if(inputPassword.length>0)
            inputPassword.substring(0, inputPassword.length-1)

        _userMessage.postValue("senha: ${inputPassword}")
    }

    private fun handlePasswordCaracterInput() {
        println("handlePasswordCaracter")

        inputPassword+="*"
        _userMessage.postValue("senha: ${inputPassword}")
    }

    private fun finishPasswordInput() {
        println("finishPasswordInput")
        _userMessage.postValue("PROCESSANDO...")
    }

    private fun creatViewToDisplayPasswordInput() {
        println("creating view to display password")
        _userMessage.postValue("senha: ")
    }

    private fun displayUserMessage(message: String) {
        _userMessage.postValue(message)
    }

    private fun stopLoadingAnimation() {
        _animation.postValue(false)
    }

    private fun handlePaymentFailure(exception: Throwable) {
        Log.e("handlePaymentFailure","")
        paymentRequest?.cancel()
        if(!failedPayment) {
            failedPayment = true
            val bundle = Bundle()

            bundle.putInt("value", total)
            bundle.putString("typeSale", typeSale)
            bundle.putString("extra", extra)

            bundle.putBoolean("error", true)
            bundle.putString("errorMsg", exception.message)

            mActivity?.runOnUiThread {
                navController?.navigate(
                    R.id.action_transactionFragment_to_transactionFailureFragment,
                    bundle
                )
            }
        }
    }

    private fun handleSucessfullPayment(response: SmartPOSPaymentResponse) {
        Log.e("handleSucessfullPayment","")
//        showDialogCustomer(response)
        cardDetection(response)
    }

    private fun cardDetection(response: SmartPOSPaymentResponse) {
        val request = SmartPOSPlugin.createCardDetectionRequestBuilder()
            .cardDetectionType(CardDetectionTypeRequestField.CardDetectionType.REMOVED)
            .callback(object : Callback<SmartPOSCardDetectionResponse>() {
                override fun onSuccess(responseCard: SmartPOSCardDetectionResponse) {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (responseCard.cardWasDetected) {
//                            showDialogCustomer(response)
                            println("onSuccess")
                            _userMessage.postValue("REMOVA O CARTÃO")
                        }
                    }
                }

                override fun onFail(error: Throwable) {
                }

                override fun onComplete() {
                    showDialogCustomer(response)
                    println("onComplete")
                }

            })
            .build()
        Zoop.post(request)

    }

    private fun showDialogCustomer(response: SmartPOSPaymentResponse) {
        try {
            // abrir dialog aqui

            mActivity?.runOnUiThread {
                dialog = CustomersCopyDialog(mActivity!!, response.transactionData)
                //dialog!!.create()
                dialog!!.show()

                dialog!!.setOnPrintClickListener{
                    //printReceipt(response.transactionData)
                    printCustomerReceipt(response)
                    handlePrintFinished(response.transactionData)
                }

                dialog!!.setOnCancelClickListener {
                    handlePrintFinished(response.transactionData)
                }
            }

        }catch (e: Exception){
            Log.e("Transaction: ", e.toString())
        }
    }

    private fun startLoadingAnimation() {
        _animation.postValue(true)
    }

    private fun printReceipt(transactionData: TransactionData) {
        val transactionData = transactionData
        val request = SmartPOSPlugin.createPrintRequestBuilder()
            .printData(Printer.PrintData(transactionData = transactionData))
            .receiptType(ReceiptType.CUSTOMER)
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
        bundle.putInt("value", total)
        bundle.putString("typeSale", typeSale)
        bundle.putString("extra", extra)
        mActivity?.runOnUiThread {
            dialog?.hide()
            navController?.navigate(
                R.id.action_transactionFragment_to_transactionCompletedFragment,
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