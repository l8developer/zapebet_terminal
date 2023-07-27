package com.bet.mpos.ui.menu.report

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentReportBinding


class ReportFragment : Fragment() {

    private var selectedOption: String = ""

    private var _binding: FragmentReportBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(ReportViewModel::class.java)

        _binding = FragmentReportBinding.inflate(inflater, container, false)

        observeViewModel(viewModel)
        buttons(viewModel)
        configAdapter()
        showToolbarLogo()

        val root: View = binding.root
        return root
    }

    private fun buttons(viewModel: ReportViewModel) {
        binding.srlReportFragment.setOnRefreshListener{ viewModel.refresh(binding.spnDaysReport.selectedItemPosition) }

        binding.spnDaysReport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                 selectedOption = parent.getItemAtPosition(position).toString()
                binding.tvTransactionDays.text = "Transacionado $selectedOption"
                viewModel.refresh(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        
        binding.btnCredit.setOnClickListener {click -> viewModel.credit_click(findNavController(), selectedOption)}
        binding.btnDebit.setOnClickListener {click -> viewModel.debit_click(findNavController(), selectedOption)}
        binding.btnPix.setOnClickListener {click -> viewModel.pix_click(findNavController(), selectedOption)}

        binding.btnShowTransactions.setOnClickListener { viewModel.showTransactionsClick(findNavController(), selectedOption) }
        binding.btnPrintSummary.setOnClickListener { viewModel.printClick(findNavController(), selectedOption) }

    }

    private fun observeViewModel(viewModel: ReportViewModel) {

        viewModel.loading.observe(viewLifecycleOwner){
            if (it)
                binding.pbReport.visibility = View.VISIBLE
            else
                binding.pbReport.visibility = View.INVISIBLE

            binding.btnShowTransactions.isEnabled = !it
            binding.btnPrintSummary.isEnabled = !it
            binding.spnDaysReport.isEnabled = !it
            binding.btnDebit.isClickable = !it
            binding.btnCredit.isClickable = !it
            binding.btnPix.isClickable = !it

            binding.srlReportFragment.isRefreshing = false
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(it != null) {
               Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.reportData.observe(viewLifecycleOwner){
            binding.tvSoldGrossValue.text = it.soldGross.value
            binding.tvSoldGrossQnt.text = it.soldGross.quantity
            binding.tvLiquidSoldValue.text = it.soldLiquid.value
            binding.tvInstallmentSalesValue.text = it.installmentSales.value
            binding.tvInstallmentSalesQnt.text = it.installmentSales.quantity
            binding.tvCreditValue.text = it.credit.value
            binding.tvCreditQnt.text = it.credit.quantity
            binding.tvDebitValue.text = it.debit.value
            binding.tvDebitQnt.text = it.debit.quantity
            binding.tvPixValue.text = it.pix.value
            binding.tvPixQnt.text = it.pix.quantity
        }

        viewModel.emptyReport.observe(viewLifecycleOwner){ isEmpty ->
            binding.btnShowTransactions.isEnabled = !isEmpty
            binding.btnPrintSummary.isEnabled = !isEmpty
            binding.btnDebit.isClickable = !isEmpty
            binding.btnCredit.isClickable = !isEmpty
            binding.btnPix.isClickable = !isEmpty
        }
    }

    private fun configAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.report_days_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnDaysReport.adapter = adapter
        }
    }

    fun showToolbarLogo(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = activity?.getDrawable(R.drawable.ic_logo_small_white)
        toolbar?.foregroundGravity = Gravity.CENTER
        toolbar?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}