package com.bet.mpos.ui.sale

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentSaleBinding
import com.bet.mpos.dialogs.ErrorNotificationDialog
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.terminal.PublicDevice
import com.zoop.sdk.api.terminal.System
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import java.lang.ref.WeakReference


class SaleFragment : Fragment() {

    private var configError: Boolean = false

    private var dialog: ErrorNotificationDialog? = null
    private var _binding: FragmentSaleBinding? = null
    private var navController: NavController? = null

    private val binding get() = _binding!!

    private lateinit var fragmentManager: FragmentManager
    private lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(SaleViewModel::class.java)
        dialog = ErrorNotificationDialog(this.requireActivity())

        _binding = FragmentSaleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observeViewModel(viewModel)
        buttons(viewModel)

        return root
    }

    private fun enableKey(state: Boolean) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(requireView())
    }

    private fun observeViewModel(viewModel: SaleViewModel){

        viewModel.value.observe(viewLifecycleOwner){
            binding.tvTotal.text = it
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(it != null){
                dialog!!.create()
                dialog!!.setTitle(it.title)
                dialog!!.setSubTitle(it.subTitle)
                dialog!!.show()
            }
        }

        viewModel.errorConfig.observe(viewLifecycleOwner){
            configError= it
        }
    }

    private fun buttons(viewModel: SaleViewModel){

        binding.btn1.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '1') }
        binding.btn2.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '2') }
        binding.btn3.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '3') }
        binding.btn4.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '4') }
        binding.btn5.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '5') }
        binding.btn6.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '6') }
        binding.btn7.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '7') }
        binding.btn8.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '8') }
        binding.btn9.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '9') }
        binding.btn0.setOnClickListener{ viewModel.onClickNumbers(binding.tvTotal.text.toString(), '0') }

        binding.btnRemove.setOnClickListener{ viewModel.onClickRemove(binding.tvTotal.text.toString()) }
        binding.btnRemove.setOnLongClickListener{ viewModel.onLongClickRemove(); false }
        binding.btnConfirm.setOnClickListener{
            if(configError) {
                dialog!!.create()
                dialog!!.setTitle("Pixxou não configurado")
                dialog!!.setSubTitle("Peça para seu agente configuralo")
                dialog!!.show()
            }else{
                viewModel.onClickConfirm(findNavController(), binding.tvTotal.text.toString())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        dialog?.hide()
    }

    fun showToolbarLogo(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = activity?.getDrawable(R.drawable.ic_logo_small_white)
        toolbar?.foregroundGravity = Gravity.CENTER
        toolbar?.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = null
    }

    override fun onStart() {
        super.onStart()
        val viewModel = ViewModelProvider(this).get(SaleViewModel::class.java)
        showToolbarLogo()
        enableKey(false)
//        println("onStart")
    }

}