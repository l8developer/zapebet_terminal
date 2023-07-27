package com.bet.mpos.ui.sale

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
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
import com.bet.mpos.objects.DialogData
import com.bet.mpos.objects.EncryptedSerialNumber
import com.bet.mpos.objects.MenuType
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response


class   SaleViewModel : ViewModel(){

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

    fun onClickNumbers(total: String, number: Char) {
        _value.value = setValue(total, number)
    }

    fun onClickRemove(total: String) {
        _value.value = delLastNumber(total)
    }

    fun onLongClickRemove() {
        _value.value = Functions.int_to_real(0)
    }

    fun onClickConfirm(navController: NavController, total: String) {

        if(Functions.real_to_int(total) > minimumValue) {
            val bundle = Bundle()
            bundle.putInt("value", Functions.real_to_int(value.value))

//            navController.navigate(R.id.action_saleFragment_to_recievePaymentFragment, bundle)
            navController.navigate(R.id.action_nav_home_to_recievePaymentFragment2, bundle)
        }
        else
            _error.value =  DialogData(
                BetApp.getAppContext().getString(R.string.value_minimal_error_title),
                BetApp.getAppContext().getString(R.string.value_minimal_error_sub_title)
            )
    }

    private fun setValue(total: String, num: Char): String? //setar valor no display
    {
        var ret = total
        var currentValue = ""
        val valorAtual: Double
        if (total.length < digits) {
            currentValue = total.replace(",", "").replace("R$ ", "").replace(".", "")
            //            System.out.println("Curr: "+currentValue);
            if (currentValue !== "0") currentValue += num else currentValue = "" + num
            if (currentValue.length > 2) {
                var s = ""
                var c = CharArray(digits)
                c = currentValue.toCharArray()
                for (i in 0 until c.size - 2) {
                    s += c[i]
                }
                s += "."
                s += c[currentValue.length - 2]
                s += c[currentValue.length - 1]
                currentValue = s
            }
            valorAtual = java.lang.Double.valueOf(currentValue)
            ret = BetApp.getAppContext().getString(R.string.total_amount, valorAtual)
        }
        return ret
    }

    private fun delLastNumber(currentValue: String): String? {
        var currentValue = currentValue
        var ret = currentValue
        val valorAtual: Double
        if (currentValue.length > 1) {
            val value = ""
            currentValue = currentValue.replace(".", "").replace(",", "")
            //            System.out.println("af :"+currentValue);
            currentValue = currentValue.substring(0, currentValue.length - 1)
            //            System.out.println("bf :"+currentValue);
            val s = currentValue.toCharArray()
            if (currentValue.length == 1) currentValue = "0.0$currentValue"
            if (currentValue.length == 2) currentValue = "0$currentValue"
            if (currentValue.length > 2) {
                var ss = ""
                var c = CharArray(digits)
                c = currentValue.toCharArray()
                for (i in 0 until c.size - 2) {
                    ss += c[i]
                }
                ss += "."
                ss += c[currentValue.length - 2]
                ss += c[currentValue.length - 1]
                currentValue = ss
            }
            //            System.out.println("current :"+currentValue);
            valorAtual = java.lang.Double.valueOf(currentValue)
            ret = BetApp.getAppContext().getString(R.string.total_amount, valorAtual)
        } else if (currentValue.length == 1) ret = Functions.int_to_real(0)
        return ret
    }

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


}