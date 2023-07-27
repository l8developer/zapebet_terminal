package com.bet.mpos.ui.payment.meansOfPayment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.BetApp
import com.bet.mpos.databinding.FragmentMeansOfPaymentBinding

class MeansOfPaymentFragment: Fragment() {

    private var _binding: FragmentMeansOfPaymentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(MeansOfPaymentViewModel::class.java)

        _binding = FragmentMeansOfPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments)

        observeViewModel(viewModel)
        buttons(viewModel)

        return root
    }

    private fun observeViewModel(viewModel: MeansOfPaymentViewModel) {
        viewModel.value.observe(viewLifecycleOwner){
            binding.tvTotalValue.text = it
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(it.isError)
                Toast.makeText(BetApp.getAppContext(), it.message, Toast.LENGTH_SHORT).show()
        }

        viewModel.isFinancilal.observe(viewLifecycleOwner){
            if(it)
                binding.btnFinancial.visibility = View.GONE
            else
                binding.btnFinancial.visibility = View.INVISIBLE
        }
    }

    fun buttons(viewModel: MeansOfPaymentViewModel){
        binding.btnOnly.setOnClickListener{ viewModel.click_only(findNavController()) }
        binding.btnSeller.setOnClickListener{ viewModel.click_seller(findNavController()) }
        binding.btnBuyer.setOnClickListener{ viewModel.click_buyer(findNavController()) }
        binding.btnFinancial.setOnClickListener{ viewModel.click_financial(findNavController()) }
    }
}