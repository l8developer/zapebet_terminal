package com.bet.mpos.ui.payment.transactionPix

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentTransactionPixBinding


class TransactionPixFragment: Fragment() {

    private var _binding: FragmentTransactionPixBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(TransactionPixViewModel::class.java)

        _binding = FragmentTransactionPixBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments, findNavController(), requireActivity())
        observeViewModel(viewModel)
        buttons(viewModel)
        hideToolbar()

        return root
    }

    private fun buttons(viewModel: TransactionPixViewModel) {
        binding.btnCancelTransactionPix.setOnClickListener{ viewModel.cancelClick() }
    }

    private fun observeViewModel(viewModel: TransactionPixViewModel) {
        viewModel.userMessage.observe(viewLifecycleOwner){
            binding.tvDisplayMessagePix.text = it
        }

        viewModel.qrCodeImage.observe(viewLifecycleOwner){
            binding.ivPaymentQrCode3.setImageBitmap(it)
        }

        viewModel.value.observe(viewLifecycleOwner){
            binding.tvFullValuePix.text = it
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if(it)
                binding.pbTransactionPix.visibility = View.VISIBLE
            else
                binding.pbTransactionPix.visibility = View.INVISIBLE
        }
    }
    private fun hideToolbar(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()

    }
}