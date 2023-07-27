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

class TransactionCompletedViewModel: ViewModel() {

    fun start(arguments: Bundle?, findNavController: NavController) {
        if(arguments != null) {
            val extra = arguments.getString("extra")
            println(extra)
            if(extra == "bet")
                handlerBet(findNavController)

            val gson = Gson()
            val json: String? = arguments.getString("transaction_data", "")
            val obj: com.zoop.sdk.api.collection.TransactionData = gson.fromJson(json, com.zoop.sdk.api.collection.TransactionData::class.java)
            saveData(obj)
            Log.d("TransactionData: ", obj.toString())
        }
    }

    private fun handlerBet(findNavController: NavController) {

        findNavController.navigate(R.id.action_transactionCompletedFragment_to_betPrintFragment)
    }

    private fun saveData(data: com.zoop.sdk.api.collection.TransactionData) {
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