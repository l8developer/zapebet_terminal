package com.bet.mpos.ui.payment.recievePayment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.BuildConfig
import com.bet.mpos.databinding.FragmentRecievePaymentBinding
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.terminal.PublicDevice
import com.zoop.sdk.api.terminal.System
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import java.lang.ref.WeakReference


class RecievePaymentFragment: Fragment() {

    private var value = 0
    private var extra: String = ""

    private var _binding: FragmentRecievePaymentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(RecievePaymentViewModel::class.java)
        _binding = FragmentRecievePaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(arguments)

        observeViewModel(viewModel)
        buttons(viewModel)

        return root
    }

    private fun observeViewModel(viewModel: RecievePaymentViewModel) {
        viewModel.value.observe(viewLifecycleOwner){
            binding.tvTotalValue.text = it
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if (it) {
                binding.clContentRecievePayment.visibility = View.INVISIBLE
                binding.pbRecievePayment.visibility = View.VISIBLE
            }else{
                binding.clContentRecievePayment.visibility = View.VISIBLE
                binding.pbRecievePayment.visibility = View.INVISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(it != null) {
                binding.clContentRecievePayment.visibility = View.INVISIBLE
                binding.tvErrorRecievePayment.visibility = View.VISIBLE
                binding.tvErrorRecievePayment.text = it
            }
        }
    }

    fun buttons(viewModel: RecievePaymentViewModel){
        binding.btnFunctionCredit.setOnClickListener{ viewModel.clickCred(findNavController()) }
        binding.btnFunctionDebit.setOnClickListener{ viewModel.clickDebit(findNavController()) }
        binding.btnFunctionPix.setOnClickListener{ viewModel.clickPix(findNavController()) }
        binding.btnFunctionMoney.setOnClickListener { viewModel.clickMoney(findNavController()) }
    }

    override fun onStart() {
        super.onStart()
        enableKeyBack(false)
    }

    private fun enableKeyBack(state: Boolean) {
        SmartPOSPlugin.createPublicDeviceRequestBuilder()
            .callback(object : Callback<WeakReference<PublicDevice>>() {
                override fun onFail(error: Throwable) {
                    Log.e("onFail", error.message.toString())
                }

                override fun onSuccess(response: WeakReference<PublicDevice>) {
                    response.get()?.setNavigationKeyEnabled(System.NavigationKey.HOME, state)
                    response.get()?.setNavigationKeyEnabled(System.NavigationKey.RECENT, state)
                    response.get()?.setNavigationKeyEnabled(System.NavigationKey.BACK, true)
                }

            }).build().run { Zoop.post(this) }
    }

}