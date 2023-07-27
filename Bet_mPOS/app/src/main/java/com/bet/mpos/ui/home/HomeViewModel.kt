package com.bet.mpos.ui.home

import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentActivity
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
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.ClientData
import com.bet.mpos.objects.DialogData
import com.bet.mpos.objects.EncryptedSerialNumber
import com.bet.mpos.objects.MenuType
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response


class HomeViewModel : ViewModel(){

    private var digits: Int = 9
    private val minimumValue = 99

    private val _value = MutableLiveData<String>().apply {}
    private val _error = MutableLiveData<DialogData>().apply {}
    private val _errorConfig = MutableLiveData<Boolean>().apply {}
    private val _unique = MutableLiveData<MenuType>().apply {}
    private val _sale = MutableLiveData<Boolean>().apply {}
    private val _bet = MutableLiveData<Boolean>().apply {}
    private val _service = MutableLiveData<Boolean>().apply {}

    var value: LiveData<String> = _value
    var error: LiveData<DialogData> = _error
    var errorConfig: LiveData<Boolean> = _errorConfig
    var unique: LiveData<MenuType> = _unique
    var sale: LiveData<Boolean> = _sale
    var bet: LiveData<Boolean> = _bet
    var service: LiveData<Boolean> = _service


    fun authentication(requireActivity: FragmentActivity) {
        val retrofit = APIClient(BuildConfig.API_URL).client
        val service = retrofit.create(APIInterface::class.java)
        val encrypt = Functions.encrypt(EncryptedSerialNumber(SerialNumber().sn).toString())
        val responseCall: Call<APIResponse> = service.getCustomerData(encrypt.first, encrypt.second)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<APIResponse> {
                override fun onResponse(
                    call: Call<APIResponse>?,
                    response: Response<APIResponse>
                ) {
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val data: ClientData = gson.fromJson(Functions.decrypt(response.body()?.ct, response.body()?.iv), ClientData::class.java)

                        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                        val encrypted =
                            ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)

                        if (encrypted != null) {
                            val gson = Gson()
                            val json = gson.toJson(data)
                            encrypted.edit()
                                .putString(
                                    BetApp.getAppContext()
                                        .getString(R.string.saved_custumer_data_file_name),
                                    json
                                )
                                .apply()
                            println("Dados atualizados")
                        }



                        if (data?.success != null){
                            if (!data?.success!!){
                                if(data?.posTerminalExist != null) {
                                    if (!data?.posTerminalExist!!) {
                                        val esp = ESharedPreferences.getInstance(
                                            BuildConfig.FILE_NAME_AD,
                                            BuildConfig.MASTER_KEY_ALIAS_AD
                                        )
                                        val editor: SharedPreferences.Editor = esp.edit()
                                        editor.putString(
                                            BetApp.getAppContext()
                                                .getString(R.string.saved_access_key),
                                            ""
                                        )
                                        editor.apply()

                                        requireActivity.finish()
                                    }
                                }
                            }
                        }

                        if(data?.customerConfigs == 1){
                            _error.value =  DialogData(
                                "Pixxou não configurado",
                                "Peça para seu agente configuralo"
                            )
                            _errorConfig.value = true
                        }


                    } else {
                        Log.e("autentication: ", response.toString())
                    }
                }

                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Log.e("autentication onFailure: ", t.message.toString())
                }
            })
        }
    }

//    fun menuOptions() {
//
//        val sale = true
//        val bet = true
//        val service = false
//
//        //UNIQUE
////        if(sale && !bet && !service)
////            _unique.value = MenuType.SALE
////        else if(bet && !sale && !service)
////            _unique.value = MenuType.BET
////        else if(service && !sale && !bet)
////            _unique.value = MenuType.SERVICE
////        else {
//            // SALE
//            _sale.value = sale
//            // BET
//            _bet.value = bet
//            // PROVIDER SERVICE
//            _service.value = service
////        }
//    }
}