package com.bet.mpos.ui.bet.BetPrint

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.databinding.FragmentBetPrintBinding
import com.bet.mpos.objects.BetCustomerRegistration
import com.bet.mpos.objects.BetGame
import com.bet.mpos.objects.BetRegistration
import com.bet.mpos.util.CPFUtil
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.GenerateBitmap
import com.bet.mpos.util.pax.PrinterTester
import com.google.api.client.json.Json
//import com.zoop.sdk.api.collection.TransactionData
import com.bet.mpos.objects.pixcred.TransactionData
import retrofit2.Call
import retrofit2.Response

class BetPrintViewModel : ViewModel() {

    private lateinit var binding: FragmentBetPrintBinding
    private val printer = PrinterTester.getInstance()

    private val _error = MutableLiveData<Boolean>().apply {}
    private val _loading = MutableLiveData<Boolean>().apply {}

    var error: LiveData<Boolean> = _error
    var loading: LiveData<Boolean> = _loading
    fun start(binding: FragmentBetPrintBinding) {
        this.binding = binding
        print()
    }

    fun print() {
        val betGame = readBetGame()
        val transactionData = readTransactionData()
        val customer = readBetDocumentForPix()

        if(betGame != null && transactionData != null && customer != null) {
            registerBet(betGame, customer, transactionData)

        }
        else{
            Log.e("betGame", betGame.toString())
            Log.e("transactionData", transactionData.toString())
            Log.e("customer", customer.toString())
        }
    }

    private fun registerBet(betGame: BetGame, customer: BetCustomerRegistration, transactionData: TransactionData)
    {
        _loading.value = true

        val retrofit = APIClient(BuildConfig.API_BET_URL).client
        //val retrofit = APIClient("http://192.168.100.94:8000/api/").client
        val service = retrofit.create(APIInterface::class.java)
        //BuildConfig.ZB_TOKEN
        //"seEuNaoApostarOdescontoEmaior"
        val responseCall: Call<Json> = service.store_bet(BuildConfig.ZB_TOKEN, customer.id.toString(), Integer.parseInt(betGame.id), transactionData.value)
        //val responseCall: Call<Json> = service.store_bet("seEuNaoApostarOdescontoEmaior", customer.id, 49620, transactionData.value)

        //println("customer_id: " + customer.id)
        //println("bet_odd: " + betGame.id)
        //println("value: " + transactionData.value!!)

        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<Json> {
                override fun onResponse(
                    call: Call<Json>?,
                    response: Response<Json>
                ) {
                    if (response.isSuccessful)
                    {
                        println("Bet registration successful")
                        println(response.body().toString())
                        handleSuccess(betGame, customer, transactionData)
                    }
                    else
                    {
                        if (response.code() == 422)
                        {
                            val responseError = response.errorBody()
                            if (responseError != null)
                            {
                                val error = responseError.string()

                                Log.e("Bet Registration: 422", error)
                            }
                        }
                        _error.value = true
                        handleFail()
                        Log.e("Bet Registration onFailure: ", response.toString())
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<Json>, t: Throwable) {
                    Log.e("Bet Registration onFailure: ", t.message.toString())
                    //handleFailed(PixcredApp.getAppContext().getString(R.string.error_generic_api))
                    _loading.value = false
                    _error.value = true
                    handleFail()
                }
            })
        }
    }

    fun readTransactionData(): TransactionData? {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_transaction_data_file_name), ""
            )
            val obj: TransactionData =
                gson.fromJson(json, TransactionData::class.java)

            return obj
        }catch(e: Throwable){
            return null
            Log.e("readTransactionData", e.toString())
        }
    }



    private fun readBetDocumentForPix(): BetCustomerRegistration? {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json2: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_bet_document_file_name), ""
            )
            val obj: BetCustomerRegistration = gson.fromJson(json2, BetCustomerRegistration::class.java)

            return obj
        }catch (e: Exception) {
            return null
            Log.e("loadHeader", e.toString())
        }
    }

    private fun readBetGame(): BetGame? {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json2: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_bet_game_file_name), ""
            )
            val obj: BetGame = gson.fromJson(json2, BetGame::class.java)

            return obj
        }catch (e: Exception) {

            return null
            Log.e("loadHeader", e.toString())
        }
    }

    fun toggleError()
    {
        _error.value = _error.value != true
    }

    private fun handleSuccess(betGame: BetGame, customer: BetCustomerRegistration, transactionData: TransactionData)
    {
        binding.tvUserMessageBetPrint.text = "Comprovante impresso"
        Thread {
            printer.init()
            val bitmap = GenerateBitmap.drawBetProof(CPFUtil.formatCPF(customer.document_value), betGame.game, betGame.location, transactionData.date+"-"+ transactionData.hour)
            printer.printBitmap(bitmap)
            printer.step(70)
            var res = printer.start()
            if (res.first)
//                printSucess()
            else
//                printError(res.second)
                Log.e("clickPrint", res.second)
        }.start()
    }

    private fun handleFail()
    {
        binding.tvUserMessageBetPrint.text = BetApp.getAppContext().getString(R.string.error_generic_api)
    }
}