package com.bet.mpos.ui.menu.ajust.updateConfigs

import android.os.Handler
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
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.ClientData
import com.bet.mpos.objects.EncryptedSerialNumber
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response

class UpdateConfigsViewModel : ViewModel() {

    private val _userData = MutableLiveData<ClientData>()
    private val _userMessage = MutableLiveData<String>()

    var userData: LiveData<ClientData> = _userData
    var userMessage: LiveData<String> = _userMessage

    fun bootData(findNavController: NavController) {

        val retrofit = APIClient(BuildConfig.API_URL).client
        val service = retrofit.create(APIInterface::class.java)
        val encrypt = Functions.encrypt(EncryptedSerialNumber(SerialNumber().sn).toString())

        val responseCall: Call<APIResponse> = service.getCustomerData(encrypt.first, encrypt.second)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<APIResponse> {
                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val data: ClientData = gson.fromJson(Functions.decrypt(response.body()?.ct, response.body()?.iv), ClientData::class.java)

                        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                        val encrypted =
                            ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)

                        if(encrypted != null) {
                            val gson = Gson()
                            val json = gson.toJson(data)
                            encrypted.edit()
                                .putString(
                                    BetApp.getAppContext().getString(R.string.saved_custumer_data_file_name),
                                    json)
                                .apply()
                        }
                        _userMessage.value = "Configurações atualizadas"
                        _userData.value = data

                        Handler().postDelayed(Runnable {
                            findNavController?.navigateUp()
                        }, 3000)

                    } else {
                        Log.e("Autentication: ", response.toString())
                    }
                }

                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Log.e("Autentication onFailure: ", t.message.toString())
                }
            })
        }
    }
}