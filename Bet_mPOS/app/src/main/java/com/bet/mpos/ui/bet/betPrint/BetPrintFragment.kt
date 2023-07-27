package com.bet.mpos.ui.bet.BetPrint

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentBetPrintBinding

class BetPrintFragment : Fragment() {

    private var _binding: FragmentBetPrintBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = BetPrintFragment()
    }

    private lateinit var viewModel: BetPrintViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBetPrintBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BetPrintViewModel::class.java)

        viewModel.start(binding)
        observeViewModel()
        buttons()


//        Handler().postDelayed(Runnable {
//            binding.tvUserMessageBetPrint.text = "Comprovante impresso"
//            binding.progressBar2.visibility = View.GONE
//            binding.btnHomeBetPrint.setOnClickListener { click ->
//                findNavController().navigate(R.id.action_betPrintFragment_to_nav_value_entry)
//            }
//        }, 4000)
    }

    private fun buttons() {
        binding.btnHomeBetPrint.setOnClickListener { click ->
            findNavController().navigate(R.id.action_betPrintFragment_to_nav_value_entry)
        }

        binding.betPrint.setOnClickListener{ click ->
            viewModel.toggleError()
            viewModel.print()
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            if(isLoading){
                binding.progressBar2.visibility = View.VISIBLE
                binding.btnHomeBetPrint.visibility = View.INVISIBLE
                //binding.tvUserMessageBetPrint.visibility = View.INVISIBLE
                binding.betPrint.visibility = View.GONE
            }else{
                binding.progressBar2.visibility = View.INVISIBLE
                binding.btnHomeBetPrint.visibility = View.VISIBLE
                //binding.tvUserMessageBetPrint.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner){ error ->
            if(error)
            {
                binding.betPrint.visibility = View.VISIBLE
                binding.btnHomeBetPrint.visibility = View.GONE
            }
            else
            {
                binding.betPrint.visibility = View.GONE
            }

        }
    }

}