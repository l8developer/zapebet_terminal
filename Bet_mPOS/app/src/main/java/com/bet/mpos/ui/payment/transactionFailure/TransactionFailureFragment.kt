package com.bet.mpos.ui.payment.transactionFailure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentTransactionFailureBinding

class TransactionFailureFragment: Fragment() {

    private var _binding: FragmentTransactionFailureBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(TransactionFailureViewModel::class.java)

        _binding = FragmentTransactionFailureBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments)
        observeViewModel(viewModel)
        hideToolbar()
        buttons(viewModel)

        return root
    }

    private fun observeViewModel(viewModel: TransactionFailureViewModel) {
        viewModel.errorMsg.observe(viewLifecycleOwner){
            binding.tvError.text = it
        }
    }

    private fun hideToolbar(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.GONE
    }

    private fun buttons(viewModel: TransactionFailureViewModel){
        binding.btnHomeTransactionFailure.setOnClickListener { findNavController().navigate(R.id.action_transactionFailureFragment_to_nav_value_entry) }

        if(arguments != null)
            binding.btnTryAgainTransactionFailure.setOnClickListener { viewModel.cancelClick(findNavController(), arguments) }
        else
            binding.btnTryAgainTransactionFailure.visibility = View.GONE
    }
}
