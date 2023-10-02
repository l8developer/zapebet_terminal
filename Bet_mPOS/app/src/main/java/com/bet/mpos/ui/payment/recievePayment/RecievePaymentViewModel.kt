package com.bet.mpos.ui.payment.recievePayment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.Fees
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class RecievePaymentViewModel: ViewModel(){

    private var total = 0;
    private var extra = ""

    private val _value = MutableLiveData<String>().apply {}
    private val _error = MutableLiveData<String>().apply {}
    private val _loading = MutableLiveData<Boolean>().apply {}

    var value: LiveData<String> = _value
    var error: LiveData<String> = _error
    var loading: LiveData<Boolean> = _loading

    fun start(arguments: Bundle?) {
        _loading.value = true
        if(arguments != null) {
            total = arguments.getInt("value")
            extra = arguments.getString("extra", "")

            _value.value = Functions.int_to_real(total)
            calculatedValue()
        }
        else
            _value.value = Functions.int_to_real(0)
    }

    fun clickCred(navController: NavController) {
        if(Functions.isConnected()){
            val bundle = Bundle()
            bundle.putInt("value", total)
            bundle.putString("typeSale", "buyer")
            bundle.putString("extra", extra)

            navController.navigate(R.id.action_recievePaymentFragment_to_transactionFragment, bundle)
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }

    fun clickDebit(navController: NavController) {
        if(Functions.isConnected()) {

            val bundle = Bundle()
            bundle.putInt("value", total)
            bundle.putString("typeSale", "debit")
            bundle.putString("extra", extra)
            navController.navigate(R.id.action_recievePaymentFragment_to_transactionFragment, bundle)
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }

    fun clickPix(navController: NavController) {
        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putInt("value", total)
            bundle.putString("typeSale", "pix")
            bundle.putString("extra", extra)
            navController.navigate(R.id.action_recievePaymentFragment_to_transactionPixFragment, bundle)
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }

    fun clickMoney(navController: NavController) {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = (calendar.get(Calendar.MONTH)+1).toString()
        if(month.length == 1)
            month = "0$month"
        val year = calendar.get(Calendar.YEAR)
        var hour = calendar.get(Calendar.HOUR_OF_DAY).toString()
        if(hour.length == 1)
            hour = "0$hour"
        var minutes = calendar.get(Calendar.MINUTE).toString()
        if(minutes.length == 1)
            minutes = "0$minutes"

        val date = "${day}/${month}/${year}"
        val time = "${hour}:${minutes}"


        val mTransactionData = com.bet.mpos.objects.pixcred.TransactionData(
            total,
            9,
            0,
            "approved",
            "Dinheiro",
            "",
            "",
            "",
            "Dinheiro",
            "",
            "",
            "",
            "",
            date,
            time,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "Pagamento em dinheiro"
        )


        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putSerializable("transactionData", mTransactionData)
            bundle.putInt("value", total)
            bundle.putString("typeSale", "money")
            bundle.putString("extra", extra)
            navController.navigate(R.id.action_recievePaymentFragment_to_transactionCompletedFragment, bundle)
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }

    private fun valueFeeJson() : String {
        return "{"+
                "\"amount_gross\"" + ":" + total +","+
                "\"serial_number\"" + ":\"" + SerialNumber().sn +"\""+
                "}"
    }
    private fun calculatedValue(){
        val retrofit = APIClient(BuildConfig.API_URL).client
        val service = retrofit.create(APIInterface::class.java)
        //val responseCall: Call<Fees> = service.getCustomerFee(Functions.real_to_int(_value.value))
        println(valueFeeJson())
        val encrypted = Functions.encrypt(valueFeeJson())
        val responseCall: Call<APIResponse> = service.getCustomerFee(encrypted.first, encrypted.second)
        println(encrypted.first)
        println(encrypted.second)
        //println(Functions.decrypt("XYg8lsjq14E3UtS6bbstnw==", "04f4a70b17bdd16ac336c13b47647706"))
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<APIResponse> {
                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    if (response.isSuccessful) {
                        _loading.value = false
                        //val data = response.body() as Fees
                        println("isSuccessfull")
                        val gson = Gson()
                        val data: Fees? = gson.fromJson(Functions.decrypt(response.body()?.ct, response.body()?.iv), Fees::class.java)
                        println(data.toString())
                        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                        val esp =  ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
                        val editor = esp.edit()
                        //val gson = Gson()
                        val json = gson.toJson(data)
                        editor.putString(BetApp.getAppContext().getString(R.string.saved_fee_file_name), json)
                        editor.commit()

                        println(json)

                    } else {
                        _loading.value = false
                        _error.value = BetApp.getAppContext().getString(R.string.error_generic_api)
                        Log.e("onFailure: ", response.message())
                    }
                }

                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    _loading.value = false
                    _error.value = BetApp.getAppContext().getString(R.string.error_generic_api)
                    Log.e("onFailure: ", t.toString())
                }
            })
        }
    }
}