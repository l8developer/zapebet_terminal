package com.bet.mpos.ui.payment.transactionCompleted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentTransactionCompletedBinding

class TransactionCompletedFragment: Fragment() {

    private var _binding: FragmentTransactionCompletedBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(TransactionCompletedViewModel::class.java)

        _binding = FragmentTransactionCompletedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments, findNavController())
        hideToolbar()
        buttons()

        return root
    }

    private fun hideToolbar(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.GONE
    }

    private fun buttons(){
        binding.btnHomeTransactionCompleted.setOnClickListener { findNavController().navigate(R.id.action_transactionCompletedFragment_to_nav_value_entry) }
    }
}