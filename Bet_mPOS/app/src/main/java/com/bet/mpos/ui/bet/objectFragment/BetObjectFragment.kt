package com.bet.mpos.ui.bet.objectFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bet.mpos.R
import com.bet.mpos.adapters.AdapterProducts
import com.bet.mpos.databinding.FragmentBetObjectBinding

class BetObjectFragment : Fragment() {

    private var _binding: FragmentBetObjectBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = BetObjectFragment()
    }

    private lateinit var viewModel: BetObjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBetObjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BetObjectViewModel::class.java)

        viewModel.start(findNavController(), requireActivity(), arguments)

        observeViewModel()
        buttons()

    }

    private fun buttons() {
        binding.tlProductObject.addTab(binding.tlProductObject.newTab().setText("Brasileirão"))
        binding.tlProductObject.addTab(binding.tlProductObject.newTab().setText("Em breve"))
        binding.tlProductObject.addTab(binding.tlProductObject.newTab().setText("Em breve"))


    }

    private fun observeViewModel() {

        viewModel.list.observe(viewLifecycleOwner){ list ->
            val adapter = AdapterProducts(list, requireContext())

            viewModel.adapter(adapter, list)
            binding.rvProduct.layoutManager = LinearLayoutManager(requireContext())
            binding.rvProduct.adapter = adapter

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

//        viewModelShared.quant.removeObservers(viewLifecycleOwner)
//        viewModelShared.total.removeObservers(viewLifecycleOwner)
//
//        viewModel.quant.removeObservers(viewLifecycleOwner)
//        viewModel.total.removeObservers(viewLifecycleOwner)
//
//        requireActivity().viewModelStore.clear()
        viewModelStore.clear()

    }

        override fun onStart() {
        super.onStart()
        showToolbarLogo()
    }

    fun showToolbarLogo(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = activity?.getDrawable(R.drawable.ic_logo_small_white)
        toolbar?.foregroundGravity = Gravity.CENTER
        toolbar?.visibility = View.VISIBLE
    }
}