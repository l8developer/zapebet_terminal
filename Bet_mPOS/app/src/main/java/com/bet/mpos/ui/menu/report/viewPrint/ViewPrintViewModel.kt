package com.bet.mpos.ui.menu.report.viewPrint

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.api.pojo.ClientData
import com.bet.mpos.api.pojo.ReportTransactions
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.FunctionsK
import com.bet.mpos.util.GenerateBitmap
import com.bet.mpos.util.pax.PrinterTester

class ViewPrintViewModel: ViewModel() {

    private var data: ReportTransactions? = null
    private var days: String? = null
    private val printer = PrinterTester.getInstance()
    private lateinit var bitmap: Bitmap

    private val _image = MutableLiveData<Bitmap>()

    val image: LiveData<Bitmap> = _image
    fun start(arguments: Bundle) {
        if(arguments != null) {

            days = arguments.getString("days", "Hoje")
            var reportData = arguments.getString("reportData", "")
            val gson = Gson()
            val json: String? = reportData
            val obj: ReportTransactions = gson.fromJson(json, ReportTransactions::class.java) as ReportTransactions
            data = obj

            generateReportSales()
        }
    }

    fun clickPrint() {
        Thread {
            printer.init()

            printer.printBitmap(bitmap)
            printer.step(70)
            var res = printer.start()
            if (res.first)
//                printSucess()
            else
//                printError(res.second)
                Log.e("clickPrint", res.second)
        }.start()
    }

    private fun generateReportSales(){
        var day = 1
        when (days){
            "Hoje" ->  day = 1
            "Ontem" -> day = 0
            "7 Dias" -> day = 7
            "30 Dias" -> day = 30
        }

        val date = FunctionsK().getRangeOfDays(day)

        bitmap = GenerateBitmap.drawReportSales(
            readCustomerData(),
            data,
            date.first,
            date.second
        )
        _image.value = bitmap
    }

    fun readCustomerData(): ClientData? {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json2: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_custumer_data_file_name), ""
            )
            val obj: ClientData = gson.fromJson(json2, ClientData::class.java)

            return obj
        }catch (e: Exception) {
            Log.e("loadHeader", e.toString())
            return null
        }
    }
}