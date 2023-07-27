package com.bet.mpos.ui.payment.transactionFailure

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.bet.mpos.R

class TransactionFailureViewModel: ViewModel(){

    private val _errorMsg = MutableLiveData<String>().apply {}

    var errorMsg: LiveData<String> = _errorMsg
    fun start(arguments: Bundle?) {
        if(arguments != null) {
            val error = arguments.getBoolean("error")

            if(error){
                _errorMsg.value = arguments.getString("errorMsg", "")
            }
        }
    }

    fun cancelClick(navController: NavController, arguments: Bundle?) {
        val typeSale = arguments?.getString("typeSale")
        if(typeSale == "pix")
            navController.navigate(R.id.action_transactionFailureFragment_to_transactionPixFragment, arguments)
        else
            navController.navigate(R.id.action_transactionFailureFragment_to_transactionFragment, arguments)
    }

}