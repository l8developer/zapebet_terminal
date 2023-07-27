package com.bet.mpos.ui.bet.betDocument

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.objects.BetCustomerRegistration
import com.bet.mpos.util.CPFUtil
import com.bet.mpos.util.ESharedPreferences
import retrofit2.Call
import retrofit2.Response

class BetDocumentViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>().apply {}

    var loading: LiveData<Boolean> = _loading

    private lateinit var mNavController: NavController

    private var value = 0
    fun start(arguments: Bundle) {

        if(arguments != null){
            value = arguments.getInt("value", 0)
        }
    }

    fun confirmClick(navController: NavController, document: String, phone: String) {
        mNavController = navController
        _loading.value = true
        if(CPFUtil.validateCPF(document)) {
            customerRegistration(
                document.replace(".","").replace("-",""),
                phone.replace("(", "").replace(")", "").replace("-","")
            )
        }else{
            handleFailed("CPF inválido")
        }
    }

    fun customerRegistration(document: String, phone: String) {
        val retrofit = APIClient(BuildConfig.API_BET_URL).client
        val service = retrofit.create(APIInterface::class.java)
        val responseCall: Call<BetCustomerRegistration> = service.customer_registration(BuildConfig.ZB_TOKEN, document, phone)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<BetCustomerRegistration> {
                override fun onResponse(
                    call: Call<BetCustomerRegistration>?,
                    response: Response<BetCustomerRegistration>
                ) {
                    if (response.isSuccessful) {
                        println(response.body().toString())
                        handleSuccess(response.body())
                    } else {
                        if(response.code() == 422) {
                             val responseError = response.errorBody()
                            if(responseError != null) {
                                val error = responseError.string()
                                if(error.contains("document") && error.contains("phone_number"))
                                    handleFailed("Documento e telefone invalído")
                                else if(error.contains("document"))
                                    handleFailed("Documento invalído")
                                else if(error.contains("phone_number"))
                                    handleFailed("Telefone invalído")

                                Log.e("customerRegistration: 422", error)
                            }
                        }
                        else{
                            Log.e("customerRegistration: ", response.toString())
                            handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                        }
                    }
                }

                override fun onFailure(call: Call<BetCustomerRegistration>, t: Throwable) {
                    Log.e("customerRegistration onFailure: ", t.message.toString())
                    handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                }
            })
        }
    }

    private fun handleFailed(errorMessage: String) {
        _loading.value = false
        Toast.makeText(BetApp.getAppContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess(customer: BetCustomerRegistration?) {
        saveDocument(customer)
        val bundle = Bundle()
        bundle.putInt("value", value)
        bundle.putString("extra", "bet")
        //bundle.putString("typeSale", "buyer")

        _loading.postValue(false)

        mNavController.navigate(
            R.id.action_betDocumentFragment_to_recievePaymentFragment,
            bundle
        )
    }

    fun saveDocument(customer: BetCustomerRegistration?) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encrypted =
            ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)

        val gson = Gson()
        val json = gson.toJson(customer)

        if(encrypted != null) {
            encrypted.edit()
                .putString(
                    BetApp.getAppContext().getString(R.string.saved_bet_document_file_name),
                    json)
                .apply()
        }
    }

}