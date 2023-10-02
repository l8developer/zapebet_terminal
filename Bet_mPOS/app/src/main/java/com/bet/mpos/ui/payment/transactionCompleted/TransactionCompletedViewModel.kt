package com.bet.mpos.ui.payment.transactionCompleted

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.objects.pixcred.TransactionData

class TransactionCompletedViewModel: ViewModel() {

    fun start(arguments: Bundle?, findNavController: NavController) {
        if(arguments != null) {
            val extra = arguments.getString("extra")
            println(extra)

            val gson = Gson()
            val json: String? = arguments.getString("transaction_data", "")
            val obj: TransactionData = gson.fromJson(json, TransactionData::class.java)
            saveData(obj)
            Log.d("TransactionData: ", obj.toString())

            if(extra == "bet")
                handlerBet(findNavController)


        }
    }

    private fun handlerBet(findNavController: NavController) {

        findNavController.navigate(R.id.action_transactionCompletedFragment_to_betPrintFragment)
    }

    private fun saveData(data: TransactionData) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encrypted =
            ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)

        if(encrypted != null) {
            val gson = Gson()
            val json = gson.toJson(data)
            encrypted.edit()
                .putString(
                    BetApp.getAppContext().getString(R.string.saved_transaction_data_file_name),
                    json)
                .apply()
        }
    }

}