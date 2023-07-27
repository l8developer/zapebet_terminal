package com.bet.mpos.ui.payment.installments

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
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.ClientData
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import com.bet.mpos.api.pojo.Fees
import com.bet.mpos.api.pojo.Financial
import com.bet.mpos.objects.Installments
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response
import java.util.*

class InstallmentsViewModel: ViewModel() {

    private var total = 0;
    private var extra = ""
    private var typeSale = ""
    private var financialPercentage = ""

    private val _value = MutableLiveData<String>().apply {}
    private val _listInstallments = MutableLiveData<ArrayList<Installments>>().apply {}
    private val _installmentType = MutableLiveData<String>().apply {}
    private val _error = MutableLiveData<String>().apply {}
    private val _loading = MutableLiveData<Boolean>().apply {}

    var value: LiveData<String> = _value
    var listInstallments: LiveData<ArrayList<Installments>> = _listInstallments
    var installmentType: LiveData<String> = _installmentType
    var error: LiveData<String> = _error
    var loading: LiveData<Boolean > = _loading

    fun start(arguments: Bundle?) {
        if(arguments != null) {
            total = arguments.getInt("value")
            typeSale = arguments.getString("typeSale", "")
            extra = arguments.getString("extra", "")
            financialPercentage = arguments.getString("financialPercentage", "")


            _value.value = Functions.int_to_real(total)

            when(typeSale){
                "seller" -> _installmentType.value = "parcelado vendedor"
                "buyer" -> _installmentType.value = "parcelado comprador"
                else -> _installmentType.value = ""
            }
            if(financialPercentage == "")
                load_fees()
            else
            {
                //println("testing!")
                _loading.value = true
                load_financial()
            }

        }
        else
            _value.value = Functions.int_to_real(0)
    }

    private fun load_fees() {

        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)

            val gson = Gson()
            val json2: String? =
                esp.getString(BetApp.getAppContext().getString(R.string.saved_fee_file_name), "")
            val obj: Fees = gson.fromJson(json2, Fees::class.java)
            val list: ArrayList<Installments> = ArrayList<Installments>()

            println(obj.toString())
            if (typeSale == "seller") {
                obj.credit.forEach {
                    list.add(
                        Installments(
                            it.installments,
                            "R$ ${Functions.int_to_real(it.total_value)}",
                            "R$ ${Functions.int_to_real(it.installment_value)}"
                        )
                    )
                }
            } else {
                if (isPixxou()) {
                    obj.buyer_mdr_pixxou.forEach {
                        list.add(
                            Installments(
                                it.installments,
                                "R$ ${Functions.int_to_real(it.total_value)}",
                                "R$ ${Functions.int_to_real(it.installment_value)}"
                            )
                        )
                    }
                } else {
                    obj.buyer_mdr.forEach {
                        list.add(
                            Installments(
                                it.installments,
                                "R$ ${Functions.int_to_real(it.total_value)}",
                                "R$ ${Functions.int_to_real(it.installment_value)}"
                            )
                        )
                    }
                }
            }

            Collections.reverse(list)
            _listInstallments.value = list
        }catch (e: Exception) {
            Log.e("loadInsallment", e.toString())
        }
    }

    private fun isPixxou(): Boolean {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json2: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_custumer_data_file_name), ""
            )
            val obj: ClientData = gson.fromJson(json2, ClientData::class.java)

            if (obj != null) {
                return obj.pixxou == 1
            } else {
                return false
            }
        }catch (e: Exception){
            Log.e("isPixxou",e.toString())
            return false
        }
    }

    private fun load_financial()
    {
        // chamada da API via Retrofit
        println("load financial")
        val list: ArrayList<Installments> = ArrayList<Installments>()
        callFinancial(list)
        //list.reverse()
        //_listInstallments.value = list
    }

    private fun financialToJson(total: Int, serialNumber: String, percentage : Int) : String {
        return "{" +
                    "\"amount_gross\": "  + total  + "," +
                    "\"serial_number\": " + "\"" + serialNumber + "\"" + "," +
                    "\"r\": "  + percentage  +
                "}"
    }

    private fun callFinancial(list : ArrayList<Installments>) {
        //println("entrou função callFinancial")
        val retrofit = APIClient(BuildConfig.API_URL).client
        val service = retrofit.create(APIInterface::class.java)
        //println(total)
        //println(SerialNumber().sn)
        //println(financialPercentage.toInt())
        println(financialToJson(total, SerialNumber().sn, financialPercentage.toInt()))
        val encrypt = Functions.encrypt(financialToJson(total, SerialNumber().sn, financialPercentage.toInt()))
        val responseCall: Call<APIResponse> = service.getTerminalFinancial(encrypt.first, encrypt.second)
        print(encrypt.first)
        println(encrypt.second)
        //println(Functions.decrypt("XYg8lsjq14E3UtS6bbstnw==", "04f4a70b17bdd16ac336c13b47647706"))
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<APIResponse> {
                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    //println("deu resposta")
                    //println(response.body()?.ct)
                    //println(response.body()?.iv)
                    if (response.isSuccessful) {
                        //println(response.body()?.ct)
                        //println(response.body()?.iv)
                        val gson = Gson()
                        val data: Financial? = gson.fromJson(Functions.decrypt(response.body()?.ct, response.body()?.iv), Financial::class.java)


                        if (data != null) {
                            var i = 0
                            data.result.financeira.forEach{
                                if(it != null)
                                    list.add(Installments(it.installments, "R$ ${Functions.int_to_real(it.amountFinal)}", "R$ ${Functions.int_to_real(it.amountInstallments)}"))
                            }

//                            list.reverse()
                            _listInstallments.value = list
                            _loading.value = false
                        }

                    } else {
                        Log.e("error: ", response.message())
                    }
                }

                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Log.e("call onFailure: ", t.message.toString())
                }
            })
        }

        //return list;
    }

    fun installmentsClick(navController: NavController, pos: Int) {
        if(Functions.isConnected()) {

            val inst = _listInstallments.value?.get(pos)
            if(inst != null){
                val value = Functions.real_to_int(inst.totalFees)
                val number_installment = inst.installment

                val bundle = Bundle()
                bundle.putInt("value", value)
                bundle.putString("typeSale", typeSale)
                bundle.putInt("installments", number_installment)
                bundle.putString("extra", extra)
                navController.navigate(R.id.action_installmentsFragment_to_transactionFragment, bundle)
            }

        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }
}