package com.bet.mpos.ui.menu.ajust.changeEstablishment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bet.mpos.databinding.FragmentChangeEstablishmentBinding

class ChangeEstablishmentFragment : Fragment() {

    private var _binding: FragmentChangeEstablishmentBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ChangeEstablishmentFragment()
    }

    private lateinit var viewModel: ChangeEstablishmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChangeEstablishmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangeEstablishmentViewModel::class.java)

        viewModel.startActivation(findNavController())
        buttons()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.userMessage.observe(viewLifecycleOwner){ message ->
            binding.tvTokenMsg.text = message
        }

        viewModel.timer.observe(viewLifecycleOwner){ time ->
            binding.tvTimer.text = time
        }
    }

    private fun buttons() {
        binding.btnCancelChangeEstablishment.setOnClickListener { click ->
            viewModel.clkCancel(true)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.clkCancel(false)
    }
}