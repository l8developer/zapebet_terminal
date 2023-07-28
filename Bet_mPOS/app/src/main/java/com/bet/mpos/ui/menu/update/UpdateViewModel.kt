package com.bet.mpos.ui.menu.update

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.UpdateResponse
import com.bet.mpos.objects.State
import com.bet.mpos.util.Connection
import com.bet.mpos.util.Functions
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response

class UpdateViewModel : ViewModel() {

    private val _currentVersion = MutableLiveData<String>().apply {}
    private val _newVersion = MutableLiveData<String>().apply {}
    private val _state = MutableLiveData<State>().apply {}

    var currentVersion: LiveData<String> = _currentVersion
    var newVersion: LiveData<String> = _newVersion
    var state: LiveData<State> = _state

    fun getAppVersion() {
        _state.value = State.SHOW_LOADING

        val retrofit = APIClient(BuildConfig.API_URL).client
        val service = retrofit.create(APIInterface::class.java)
        //val responseCall: Call<Fees> = service.getCustomerFee(Functions.real_to_int(_value.value))
        val encrypted = Functions.encrypt(
            "{"
                    + "\"serial_number\"" + ":\"" + SerialNumber().sn + "\"" +
                    "}")

        val responseCall: Call<APIResponse> = service.getVersion(encrypted.first, encrypted.second)
        println(encrypted.first)
        println(encrypted.second)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<APIResponse> {
                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    //SUCCESS
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val data: UpdateResponse = gson.fromJson(Functions.decrypt(response.body()?.ct, response.body()?.iv), UpdateResponse::class.java)

                        if(data != null) {
                            _currentVersion.value = BuildConfig.VERSION_NAME
                            _newVersion.value = data.version_name

                            //UPDATE NOT AVALABLE
                            if(data.version > BuildConfig.VERSION_CODE)
                                _state.value = State.UPDATE_AVAILABLE
                            //UPDATE AVALIABLE
                            else
                                _state.value = State.UPDATE_NOT_AVAILABLE
                        }

                        _state.value = State.HIDE_LOADING
                        println(data)
                    } else {
                        _state.value = State.ERROR
                        Log.e("onFailure: ", response.toString())
                    }
                }

                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    _state.value = State.ERROR
                    Log.e("onFailure: ", t.toString())
                }
            })
        }



//        Handler().postDelayed(Runnable {
//        //SUCCESS
//            _currentVersion.value = "1.0.0"
//            _newVersion.value = "1.1.0"
////            //UPDATE NOT AVALABLE
////               _state.value = State.UPDATE_NOT_AVAILABLE
////            //UPDATE AVALIABLE
//                _state.value = State.UPDATE_AVAILABLE
//        //FAILED
////            _state.value = State.ERROR
//
//            _state.value = State.HIDE_LOADING
//        }, 2000)
    }

    fun clickUpdate(requireActivity: FragmentActivity) {

        val status = Connection().getConnectionType(requireActivity)
        when (status) {
            "Sem conexão de rede" -> {
                Toast.makeText(BetApp.getAppContext(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
            }
            "Dados móveis" -> {
                Toast.makeText(BetApp.getAppContext(), "Utilizar conexão WI-FI para atualizar o app", Toast.LENGTH_SHORT).show()
            }
            "Wi-Fi" -> {
                val packageManager = requireActivity.packageManager
                val packageName = "com.bet.update" // Pacote do aplicativo de destino
                val intent = packageManager?.getLaunchIntentForPackage(packageName)

                if (intent != null) {
                    // Abrir o aplicativo de destino
                    requireActivity.startActivity(intent)
                } else {
                    Log.e("startIntentUpdate", "null")
                }
            }
        }
    }
}