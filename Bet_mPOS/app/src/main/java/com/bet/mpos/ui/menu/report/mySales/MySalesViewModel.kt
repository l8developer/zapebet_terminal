package com.bet.mpos.ui.menu.report.mySales

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.ReportTransactionsDetails
import com.bet.mpos.objects.ReportSalesData
import com.bet.mpos.util.Functions
import com.bet.mpos.util.FunctionsK
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response
import java.util.ArrayList

class MySalesViewModel: ViewModel() {

    private var type: String = "all"
    private var sPeriod: String = ""
    private var navController: NavController? = null

    private val _period = MutableLiveData<String>().apply {}
    private val _error = MutableLiveData<String>().apply {}
    private val _loading = MutableLiveData<Boolean>().apply {}
    private val _reportDetailsData = MutableLiveData<ArrayList<ReportSalesData>>().apply {}

    var period: LiveData<String> = _period
    var error: LiveData<String> = _error
    var loading: LiveData<Boolean> = _loading
    var reportDetailsData: LiveData<ArrayList<ReportSalesData>> = _reportDetailsData

    fun start(arguments: Bundle?, _navController: NavController) {
        _loading.value = true
        navController = _navController

        if(arguments != null) {
            type = arguments.getString("type", "1,2,3")
            sPeriod = arguments.getString("period", "-")

            _period.value = "No periodo: ${sPeriod}"

            when (sPeriod) {
                "Hoje" -> loadTerminalTransactionsDetails(0)
                "Ontem" -> loadTerminalTransactionsDetails(1)
                "7 Dias" -> loadTerminalTransactionsDetails(7)
                "30 Dias" -> loadTerminalTransactionsDetails(30)
            }


        }
        else{
            _error.value = BetApp.getAppContext().getString(R.string.error_generic_api)
            navController?.navigateUp()
        }
    }

    fun reportTransactionDetailToJson(serial : String, start : String, end : String, type: String) : String
    {
        return "{" +
                "\n\"serial_number\": " + "\"" + serial + "\"" + "," +
                "\n\"start_date\": " + "\"" +  start + "\"" + "," +
                "\n\"end_date\": " + "\"" + end + "\"" + "," +
                "\n\"type\": " + "\"" + type + "\"" +
                "\n}";
    }

    private fun loadTerminalTransactionsDetails(days: Int){
        val range = FunctionsK().getRangeOfDays(days)

        val retrofit = APIClient(BuildConfig.API_URL).client
        val service = retrofit.create(APIInterface::class.java)
        val encrypt = Functions.encrypt(reportTransactionDetailToJson(SerialNumber().sn, range.first, range.second, type))
        println("ct: "+ encrypt.first)
        println("iv: "+ encrypt.second)
        val responseCall: Call<APIResponse> = service.getTerminalTransactionsDetails(encrypt.first, encrypt.second)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<APIResponse> {
                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    if (response.isSuccessful) {
                        //val data = response.body() as ReportTransactionsDetails
                        try {
                            val gson = Gson()
                            val data: ReportTransactionsDetails? = gson.fromJson(Functions.decrypt(response.body()?.ct, response.body()?.iv), ReportTransactionsDetails::class.java)
                            println(data.toString())
                            if (data != null) {
                                CalculateData(data)
                            }
                        }catch (e: Exception){
                            _loading.value = false
                            _error.value = BetApp.getAppContext().getString(R.string.error_generic_api)
                            Log.e("onFailure: ", response.message())
                        }

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

    fun CalculateData(data: ReportTransactionsDetails){
        var alist: ArrayList<ReportSalesData> = ArrayList<ReportSalesData>()
//        alist.add(ReportSalesData(
//            "day_month",
//            "R$ 903,95",
//            "APROVADO",
//            "27 ABR",
//            "14:22"))
        alist.add(ReportSalesData(
            -1,
            0,
            0,
            0,
            0,
            "",
            "No periodo: $sPeriod",
            "",
            "",
            "",
            ""
        ))

        data.result.data.forEach { days ->

            alist.add(ReportSalesData(
                -1,
                0,
                0,
                0,
                0,
                "",
                "",
                FunctionsK.convertDate(days[0].entry_date),
                "",
                "",
                ""
            ))

            days.forEach { transaction ->

                alist.add(ReportSalesData(
                    transaction.transaction_type,
                    transaction.status,
                    transaction.value_gross,
                    transaction.value_net,
                    transaction.total_installments,
                    FunctionsK.convertDate2(transaction.entry_date),
                    "",
                    transaction.entry_date,
                    transaction.card_last_dig,
                    transaction.card_flag,
                    transaction.transaction_id
                ))
            }
        }

        _loading.postValue(false)
        _reportDetailsData.postValue(alist)
    }
}