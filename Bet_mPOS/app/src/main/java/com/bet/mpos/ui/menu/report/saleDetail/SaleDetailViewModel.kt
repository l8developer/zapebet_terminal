package com.bet.mpos.ui.menu.report.saleDetail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.bet.mpos.R
import com.bet.mpos.objects.ReportSalesData

class SaleDetailViewModel: ViewModel() {

    private val _showData = MutableLiveData<ReportSalesData>().apply {}

    var showData: LiveData<ReportSalesData> = _showData

    fun start(arguments: Bundle?, navController: NavController) {
        if(arguments != null) {
            val data = arguments.getString("transaction_data")
            val gson = Gson()
            val json: String? = data
            val obj: ReportSalesData = gson.fromJson(json, ReportSalesData::class.java)
            _showData.value = obj
        }
    }

    fun btnHomeClick(navController: NavController) {
        navController.navigate(R.id.nav_report)
    }
}