package com.bet.mpos.ui.menu.report.mySales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.bet.mpos.R
import com.bet.mpos.adapters.AdapterReportSales
import com.bet.mpos.databinding.FragmentMySalesBinding

class MySalesFragment: Fragment() {

    private var _binding: FragmentMySalesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(MySalesViewModel::class.java)

        _binding = FragmentMySalesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments, findNavController())
        observeViewModel(viewModel)
        hideToolbarLogo()

        return root
    }

    private fun observeViewModel(viewModel: MySalesViewModel) {
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                binding.pbMySales.visibility = View.VISIBLE
                binding.layoutMySales.visibility = View.INVISIBLE
            }else{
                binding.pbMySales.visibility = View.INVISIBLE
                binding.layoutMySales.visibility = View.VISIBLE
            }
        }
        viewModel.reportDetailsData.observe(viewLifecycleOwner){
            val adapter = AdapterReportSales(it)
            binding.rvTransactionsList.layoutManager = LinearLayoutManager(requireContext())
            binding.rvTransactionsList.adapter = adapter

            adapter.setOnItemClickListener { position, item ->
                println(item.toString())
                if(position > 1) {
                    val gson = Gson()
                    val json = gson.toJson(item)
                    val bundle = Bundle()
                    bundle.putString("transaction_data", json)
                    findNavController().navigate(
                        R.id.action_mySalesFragment_to_fragmentSaleDetail,
                        bundle
                    )
                }
            }

        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    private fun hideToolbarLogo(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = null
    }
}