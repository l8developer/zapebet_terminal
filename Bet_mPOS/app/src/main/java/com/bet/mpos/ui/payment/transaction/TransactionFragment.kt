package com.bet.mpos.ui.payment.transaction

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentTransactionsBinding

class TransactionFragment: Fragment() {

    private var _binding: FragmentTransactionsBinding? = null

    private val binding get() = _binding!!
    private var Animation: AnimationDrawable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments, findNavController(), requireActivity())
        observeViewModel(viewModel)
        buttons(viewModel)
        hideToolbar()

        return root
    }

    private fun buttons(viewModel: TransactionViewModel) {
        binding.btnCancelTransaction.setOnClickListener { viewModel.cancelClick() }
    }

    private fun observeViewModel(viewModel: TransactionViewModel) {
        viewModel.value.observe(viewLifecycleOwner){
            binding.tvFullValue.text = it
        }

        viewModel.userMessage.observe(viewLifecycleOwner){
            binding.tvDisplayMessage.text = it
        }
        viewModel.animation.observe(viewLifecycleOwner){
            if(it){
                binding.ivAnimation.setBackgroundResource(R.drawable.animation)
                binding.ivAnimation.visibility = View.VISIBLE
                Animation = binding.ivAnimation.background as AnimationDrawable
                Animation?.start()
            }
            else{
                binding.ivAnimation.visibility = View.INVISIBLE
            }
        }
    }

    private fun hideToolbar(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
    }

}