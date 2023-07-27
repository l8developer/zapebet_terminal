package com.bet.mpos.ui.login.enable

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentEnableBinding

class EnableFragment : Fragment() {

    private var _binding: FragmentEnableBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(EnableViewModel::class.java)

        _binding = FragmentEnableBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.let { viewModel.startActivation(it, findNavController()) }
        buttons(viewModel)
        observeViewModel(viewModel)

        return root
    }

    private fun observeViewModel(viewModel: EnableViewModel) {
        viewModel.conectionError.observe(viewLifecycleOwner){
            Toast.makeText(activity, activity?.getString(R.string.error_conection_message), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        viewModel.userMessage.observe(viewLifecycleOwner){
            binding.tvUserMessageEnable.text = it
        }
        viewModel.timer.observe(viewLifecycleOwner){ timer ->
            binding.tvTimerEnable.text = timer
        }
    }

    private fun buttons(viewModel: EnableViewModel) {
        binding.btnCancelEnable.setOnClickListener { click ->
            viewModel.clkCancel()
        }
    }

    override fun onStop() {
        super.onStop()
        val viewModel = ViewModelProvider(this).get(EnableViewModel::class.java)
        viewModel.clkCancel()
    }
}