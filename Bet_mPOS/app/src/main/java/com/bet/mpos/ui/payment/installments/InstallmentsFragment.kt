package com.bet.mpos.ui.payment.installments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.adapters.AdapterInstallments
import com.bet.mpos.databinding.FragmentInstallmentsBinding

class InstallmentsFragment: Fragment() {

    private var _binding: FragmentInstallmentsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(InstallmentsViewModel::class.java)

        _binding = FragmentInstallmentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments)

        observeViewModel(viewModel)
        buttons(viewModel)

        return root
    }

    private fun buttons(viewModel: InstallmentsViewModel) {
        binding.lvInstallments.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
            viewModel.installmentsClick(findNavController(), i)
        })
    }

    private fun observeViewModel(viewModel: InstallmentsViewModel) {
        viewModel.value.observe(viewLifecycleOwner){
            binding.tvTotalValueInstallments.text = it
        }

        viewModel.installmentType.observe(viewLifecycleOwner){
            binding.tvFormOfPayment.text = it
        }

        viewModel.listInstallments.observe(viewLifecycleOwner){
//            println(it)
            val adapter = AdapterInstallments(it, requireActivity())
            binding.lvInstallments?.adapter = adapter
        }

        viewModel.loading.observe(viewLifecycleOwner){ isLoading ->

            if(isLoading){
                binding.pbInstallments.visibility = View.VISIBLE
                binding.grav.visibility = View.INVISIBLE
            }else{
                binding.pbInstallments.visibility = View.INVISIBLE
                binding.grav.visibility = View.VISIBLE
            }

        }
    }
}