package com.bet.mpos.ui.payment.meansOfPayment

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
import com.bet.mpos.api.pojo.ClientData
import com.bet.mpos.util.Functions
import com.bet.mpos.objects.ErrorContainer
import com.bet.mpos.util.ESharedPreferences

class MeansOfPaymentViewModel: ViewModel() {

    private var total = 0;
    private var extra = ""

    private val _value = MutableLiveData<String>().apply {}
    private val _error = MutableLiveData<ErrorContainer>().apply {}
    private val _isFinancial = MutableLiveData<Boolean>().apply {}


    var value: LiveData<String> = _value
    var error: LiveData<ErrorContainer> = _error
    var isFinancilal: LiveData<Boolean> = _isFinancial

    fun start(arguments: Bundle?) {
        if(arguments != null) {
            total = arguments.getInt("value")
            extra = arguments.getString("extra", "")

            _value.value = Functions.int_to_real(total)
            _isFinancial.value = true
        }
        else{
            _value.value = Functions.int_to_real(0)
        }
    }

    fun click_only(navController: NavController) {
        if(Functions.isConnected()){
            val bundle = Bundle()
            bundle.putInt("value", total)
            bundle.putString("typeSale", "only")
            bundle.putString("extra", extra)
            navController.navigate(R.id.action_meansOfPaymentFragment_to_transactionFragment, bundle)
        }
        else
            _error.value = ErrorContainer(true, BetApp.getAppContext().getString(R.string.error_conection_message))
    }

    fun click_seller(navController: NavController) {
        if(Functions.isConnected()){
            val bundle = Bundle()
            bundle.putInt("value", total)
            bundle.putString("typeSale", "seller")
            bundle.putString("extra", extra)
            navController.navigate(R.id.action_meansOfPaymentFragment_to_installmentsFragment, bundle)
        }
        else
            _error.value = ErrorContainer(true, BetApp.getAppContext().getString(R.string.error_conection_message))
    }

    fun click_buyer(navController: NavController) {
        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putInt("value", total)
            bundle.putString("typeSale", "buyer")
            bundle.putString("extra", extra)
            navController.navigate(R.id.action_meansOfPaymentFragment_to_installmentsFragment, bundle)
        }
        else
            _error.value = ErrorContainer(true, BetApp.getAppContext().getString(R.string.error_conection_message))
    }

    private fun isFinancial(): Boolean {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json2: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_custumer_data_file_name), ""
            )
            val obj: ClientData = gson.fromJson(json2, ClientData::class.java)

            if (obj != null) {
                return obj.financial == 1
            } else {
                return false
            }
        }catch (e :java.lang.Exception){
            Log.e("isFinancial",e.toString())
            return false
        }
    }

    fun click_financial(navController : NavController) {
        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putInt("value", total)
            bundle.putString("typeSale", "buyer")
            bundle.putString("extra", extra)
            //navController.navigate(R.id.action_meansOfPaymentFragment_to_financialFragment, bundle)
        }
        else
            _error.value = ErrorContainer(true, BetApp.getAppContext().getString(R.string.error_conection_message))

    }
}