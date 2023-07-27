package com.bet.mpos.ui.menu.report.saleDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentSaleDetailBinding
import com.bet.mpos.util.Functions
import com.bet.mpos.util.FunctionsK

class FragmentSaleDetail: Fragment() {

    private var _binding: FragmentSaleDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(SaleDetailViewModel::class.java)

        _binding = FragmentSaleDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments, findNavController())
        observeViewModel(viewModel)
        buttons(viewModel)
        return root
    }

    private fun observeViewModel(viewModel: SaleDetailViewModel) {

        viewModel.showData.observe(viewLifecycleOwner){
            binding.tvTitleValueLiquid.text = "R$ ${Functions.int_to_real(it.value_net)}"
            binding.tvDateSaleDetail.text = "Realizado em ${FunctionsK.convertDate3(it.entry_date)}"
            when (it.status){
                1 -> {
                    binding.tvStatusSaleDetail.text = "Aprovado"
                    binding.tvStatusSaleDetail.background =
                        BetApp.getAppContext().getDrawable(R.drawable.border_green)
                }

                2 -> {
                    binding.tvStatusSaleDetail.text = "Estorno"
                    binding.tvStatusSaleDetail.background =
                        BetApp.getAppContext().getDrawable(R.drawable.border_inactive)
                }

                3 -> {
                    binding.tvStatusSaleDetail.text = "Falha"
                    binding.tvStatusSaleDetail.background =
                        BetApp.getAppContext().getDrawable(R.drawable.border_orange)
                }
            }
            binding.tvValueGrossSaleDetail.text = "R$ ${Functions.int_to_real(it.value_gross)}"
            binding.tvMdrRateSaleDetail.text = "R$ ${Functions.int_to_real(it.value_gross - it.value_net)}"
            binding.tvSubValueLiquid.text = "R$ ${Functions.int_to_real(it.value_net)}"
            when (it.transaction_type){
                1 -> {
                    binding.tvTypeSaleDetail.text = "Debito"
                    binding.tvInstallmentsSaleDetail.text = "Debito"
                    binding.tvFinalCardSaleDetail.text = "Final - ${it.card_last_dig}"
                }
                2 -> {
                    binding.tvTypeSaleDetail.text = "Crédito"
                    binding.tvInstallmentsSaleDetail.text = "Crédito em ${it.total_installments} parcelas"
                    binding.tvFinalCardSaleDetail.text = "Final - ${it.card_last_dig}"
                }
                3 -> {
                    binding.tvTypeSaleDetail.text = "Pix"
                    binding.tvInstallmentsSaleDetail.text = "Pix"
                }
            }
            binding.tvTransactionCodeSaleDetail.text = it.transaction_id
        }

    }

    private fun buttons(viewModel: SaleDetailViewModel) {
        binding.btnHomeSaleDetail.setOnClickListener { click -> viewModel.btnHomeClick(findNavController())}
    }
}