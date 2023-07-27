package com.bet.mpos.ui.menu.report

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
import com.bet.mpos.util.Functions
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.APIResponse
import com.bet.mpos.api.pojo.ReportTransactions
import com.bet.mpos.objects.ReportData
import com.bet.mpos.objects.ReportData.Value
import com.bet.mpos.util.FunctionsK
import com.bet.mpos.util.SerialNumber
import retrofit2.Call
import retrofit2.Response

class ReportViewModel : ViewModel() {

    private var responseData: ReportTransactions? = null

    private val _error = MutableLiveData<String>().apply {}
    private val _loading = MutableLiveData<Boolean>().apply {}
    private val _reportData = MutableLiveData<ReportData>().apply {}
    private val _emptyReport = MutableLiveData<Boolean>().apply {}

    var error: LiveData<String> = _error
    var loading: LiveData<Boolean> = _loading
    var reportData: LiveData<ReportData> = _reportData
    var emptyReport: LiveData<Boolean> = _emptyReport

    fun refresh(i: Int) {
        _loading.value = true
        var days: Int = 0
        when (i) {
            0 -> days = 0
            1 -> days = 1
            2 -> days = 7
            3 -> days = 30
        }
        val range = FunctionsK().getRangeOfDays(days)

        val start = range.first
        val end = range.second

        loadTerminalTransactions(start, end)
    }

    fun CalculateData(data: ReportTransactions) {
        val soldGrossValue = Integer.parseInt(data.result.debit[0].gross) + Integer.parseInt(data.result.credit[0].gross) + Integer.parseInt(data.result.pix[0].gross)
        val soldGrossQuantity = data.result.debit[0].sales + data.result.credit[0].sales + data.result.pix[0].sales
        val soldLiquidValue = Integer.parseInt(data.result.debit[0].net) + Integer.parseInt(data.result.credit[0].net) + Integer.parseInt(data.result.pix[0].net)
        val installmentSalesGrossValue = Integer.parseInt(data.result.installments[0].gross)
        val installmentSalesGrossQuantity = data.result.installments[0].sales
        val creditGross = Integer.parseInt(data.result.credit[0].gross)
        val creditQuantity = data.result.credit[0].sales
        val debitGross = Integer.parseInt(data.result.debit[0].gross)
        val debitQuantity = data.result.debit[0].sales
        val pixGross = Integer.parseInt(data.result.pix[0].gross)
        val pixQuantity = data.result.pix[0].sales

        val soldGross = Value("R$ " + Functions.int_to_real(soldGrossValue), "$soldGrossQuantity Venda(s)")
        val soldLiquid = Value("R$ " + Functions.int_to_real(soldLiquidValue), "")
        val installmentSales = Value("R$ " + Functions.int_to_real(installmentSalesGrossValue), "$installmentSalesGrossQuantity Venda(s)")
        val credit = Value("R$ " + Functions.int_to_real(creditGross), "$creditQuantity Venda(s)")
        val debit = Value("R$ " + Functions.int_to_real(debitGross), "$debitQuantity Venda(s)")
        val pix = Value("R$ " + Functions.int_to_real(pixGross), "$pixQuantity Venda(s)")

        val reportData = ReportData(
            soldGross,
            soldLiquid,
            installmentSales,
            credit,
            debit,
            pix
        )

        _emptyReport.postValue(pixQuantity == 0 && debitQuantity == 0 && creditQuantity == 0 && soldGrossQuantity == 0 && installmentSalesGrossQuantity == 0)
        _reportData.value = reportData
    }

    fun reportTransactionToJson(serial : String, start : String, end : String) : String
    {
        return "{" +
                "\n\"serial_number\": " + "\"" + serial + "\"" + "," +
                "\n\"start_date\": " + "\"" +  start + "\"" + "," +
                "\n\"end_date\": " + "\"" + end + "\"" +
                "\n}";
    }

    private fun loadTerminalTransactions(start: String, end: String){
        val retrofit = APIClient(BuildConfig.API_URL).client
        val service = retrofit.create(APIInterface::class.java)
        //println(SerialNumber().sn)
        //println(start)
        //println(end)
        //val responseCall: Call<ReportTransactions> = service.getTerminalTransactions(
        //    SerialNumber().sn, start, end)
        println(reportTransactionToJson(SerialNumber().sn, start, end))
        val encrypt = Functions.encrypt(reportTransactionToJson(SerialNumber().sn, start, end))
        val responseCall: Call<APIResponse> = service.getTerminalTransactions(encrypt.first, encrypt.second)
//        print(encrypt.first)
//        println(encrypt.second)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<APIResponse> {
                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    if (response.isSuccessful) {
                        _loading.value = false
                        //val data = response.body() as ReportTransactions
                        val gson = Gson()
                        val data: ReportTransactions? = gson.fromJson(Functions.decrypt(response.body()?.ct, response.body()?.iv), ReportTransactions::class.java)

                        println(data?.message)
                        responseData = data

                        if (data != null) {
                            CalculateData(data)
                        }
                    } else {
                        _loading.value = false
                        _error.value = BetApp.getAppContext().getString(R.string.error_generic_api)
                        Log.e("onFailure: ", response.message())
                        println(response.message())
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

    fun showTransactionsClick(navController: NavController, selectedOption: String) {

        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putString("type", "1,2,3")
            bundle.putString("period", selectedOption)
            println(selectedOption)
            navController.navigate(
                R.id.action_nav_report_to_mySalesFragment,
                bundle
            )
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }

    fun credit_click(navController: NavController, selectedOption: String) {
        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putString("type", "2")
            bundle.putString("period", selectedOption)
            println(selectedOption)
            navController.navigate(
                R.id.action_nav_report_to_mySalesFragment,
                bundle
            )
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)

    }

    fun debit_click(navController: NavController, selectedOption: String) {
        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putString("type", "1")
            bundle.putString("period", selectedOption)
            println(selectedOption)
            navController.navigate(
                R.id.action_nav_report_to_mySalesFragment,
                bundle
            )
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }

    fun pix_click(navController: NavController, selectedOption: String) {
        if(Functions.isConnected()) {
            val bundle = Bundle()
            bundle.putString("type", "3")
            bundle.putString("period", selectedOption)
            println(selectedOption)
            navController.navigate(
                R.id.action_nav_report_to_mySalesFragment,
                bundle
            )
        }
        else
            _error.value = BetApp.getAppContext().getString(R.string.error_conection_message)
    }

    fun printClick(navController: NavController, selectedOption: String) {
        val gson = Gson()
        val json = gson.toJson(responseData)

        val bundle = Bundle()
        bundle.putString("days", selectedOption)
        bundle.putString("reportData", json)

        navController.navigate(R.id.action_nav_report_to_viewPrintFragment, bundle)
    }

}