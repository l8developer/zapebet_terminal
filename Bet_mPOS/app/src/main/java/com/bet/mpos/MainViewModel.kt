package com.bet.mpos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.ClientData
import com.bet.mpos.objects.EncryptedSerialNumber
import com.bet.mpos.util.*
import retrofit2.Call
import retrofit2.Response


class MainViewModel :  ViewModel() {

    private val _fantasyName = MutableLiveData<String>()
    private val _corporateReason = MutableLiveData<String>()
    private val _document = MutableLiveData<String>()

    val fantasyName: LiveData<String> = _fantasyName
    val corporateReason: LiveData<String> = _corporateReason
    val document: LiveData<String> = _document

    fun authentication() {

        loadHeader()

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
                        showHeader(data)

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

    private fun loadHeader() {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json2: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_custumer_data_file_name), ""
            )
            val obj: ClientData = gson.fromJson(json2, ClientData::class.java)

            showHeader(obj)
        }catch (e: Exception) {
            Log.e("loadHeader", e.toString())
        }
    }

    fun showHeader(data: ClientData?) {
        if (data != null) {
            if(data.documentValue != null) {

                if (data.socialName != null)
                    _fantasyName.postValue(data.socialName)
                else
                    _fantasyName.postValue("")

                    _corporateReason.postValue(data.registerName)

                var document = data.documentValue

                try {
                    document = MaskPix().maskPix(document)
                }catch (e: Exception){
                    Log.e("MaskPixKey", e.toString())
                }

                _document.postValue(document)
            }
        }
    }

//    fun decrypt() {
//        try {
//            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//            val encrypted = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
//            val filename = PixcredApp.getAppContext().getString(R.string.saved_value_file_name)
//            val data = separateStringBySemicolon(encrypted, filename)
//
//            if (data != null) {
//
//                val substringFantasyName = data[3].split(":")
//                val substringCorporateReason = data[2].split(":")
//                val substringDocument = data[1].split(":")
//
//                _fantasyName.value = substringFantasyName[1]
//                _corporateReason.value = substringCorporateReason[1]
//                _document.value = substringDocument[1]
//            }
//        }catch(e:Exception){
//            Log.e("LoadHeader", e.message.toString())
//        }
//    }

}