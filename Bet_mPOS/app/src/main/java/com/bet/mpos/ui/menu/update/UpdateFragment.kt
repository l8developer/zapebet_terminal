package com.bet.mpos.ui.menu.update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentUpdateBinding
import com.bet.mpos.objects.State
import com.bet.mpos.util.Functions
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.terminal.PublicDevice
import com.zoop.sdk.api.terminal.System
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import java.lang.ref.WeakReference

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = UpdateFragment()
    }

    private lateinit var viewModel: UpdateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(UpdateViewModel::class.java)
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        buttons()
        observeViewModel()
        enableKey(false)


        if(Functions.isConnected())
            viewModel.getAppVersion()
        else
        {
            binding.tvMessageUpdate.text = BetApp.getAppContext().getText(R.string.error_generic_api)
            binding.ivUpdate.visibility = View.INVISIBLE
            binding.tvMessageUpdate.visibility = View.VISIBLE
            binding.btnCancelUpdate.isEnabled = true
            binding.btnUpdate.visibility = View.INVISIBLE
            binding.btnTryAgainUpdate.visibility = View.VISIBLE
            binding.tvNewVersion.text = "-"
            binding.tvCurrentVersion.text = "-"
        }

        return root
    }

    private fun observeViewModel() {

        viewModel.currentVersion.observe(viewLifecycleOwner){
            binding.tvCurrentVersion.text = it
        }

        viewModel.newVersion.observe(viewLifecycleOwner){
            binding.tvNewVersion.text = it
        }

        viewModel.state.observe(viewLifecycleOwner){ state ->

            when(state){
                 State.UPDATE_NOT_AVAILABLE  -> {
                    binding.tvMessageUpdate.text = "App está na ultima versão "
                    binding.tvMessageUpdate.visibility = android.view.View.VISIBLE
                    binding.ivUpdate.visibility = android.view.View.VISIBLE
                    binding.btnUpdate.isEnabled = true
                    binding.ivUpdate.setImageDrawable(BetApp.getAppContext().getDrawable(R.drawable.baseline_check_24))
                }
                State.UPDATE_AVAILABLE -> {
                    binding.tvMessageUpdate.text = "Atualização disponível"
                    binding.tvMessageUpdate.visibility = View.VISIBLE
                    binding.ivUpdate.visibility = View.VISIBLE
                    binding.ivUpdate.setImageDrawable(BetApp.getAppContext().getDrawable(R.drawable.baseline_priority_high_24))
                    binding.btnUpdate.isEnabled = true
                }
                State.SHOW_LOADING -> {
                    binding.btnCancelUpdate.isEnabled = false
                    binding.btnCancelUpdate.visibility = View.VISIBLE
                    binding.btnUpdate.visibility = View.VISIBLE
                    binding.btnUpdate.isEnabled = false
                    binding.btnTryAgainUpdate.visibility = View.INVISIBLE
                    binding.tvMessageUpdate.visibility = View.GONE
                    binding.pbLoadingUpdate.visibility = View.VISIBLE
                    binding.ivUpdate.visibility = View.INVISIBLE
                }
                State.HIDE_LOADING -> {
                    binding.btnCancelUpdate.isEnabled = true
                    binding.pbLoadingUpdate.visibility = View.GONE
                }
                State.ERROR -> {
                    binding.tvMessageUpdate.text = BetApp.getAppContext().getText(R.string.error_generic_api)
                    binding.ivUpdate.visibility = View.INVISIBLE
                    binding.tvMessageUpdate.visibility = View.VISIBLE
                    binding.btnCancelUpdate.isEnabled = true
                    binding.btnUpdate.visibility = View.INVISIBLE
                    binding.btnTryAgainUpdate.visibility = View.VISIBLE
                    binding.pbLoadingUpdate.visibility = View.GONE
                    binding.tvNewVersion.text = "-"
                    binding.tvCurrentVersion.text = "-"
                }
            }
        }
    }


    private fun buttons() {
        binding.btnUpdate.setOnClickListener { click ->
            viewModel.clickUpdate(requireActivity())

//            val packageManager = context?.packageManager
//            val packageName = "com.pixcred.update" // Pacote do aplicativo de destino
//            val intent = packageManager?.getLaunchIntentForPackage(packageName)
//
//            if (intent != null) {
//                // Abrir o aplicativo de destino
//                startActivity(intent)
//            } else {
//                Log.e("startIntentUpdate", "null")
//            }
        }
        binding.btnTryAgainUpdate.setOnClickListener { click ->

            if(Functions.isConnected())
                viewModel.getAppVersion()
            else{
                binding.tvMessageUpdate.text = BetApp.getAppContext().getText(R.string.error_generic_api)
                binding.ivUpdate.visibility = View.INVISIBLE
                binding.tvMessageUpdate.visibility = View.VISIBLE
                binding.btnCancelUpdate.isEnabled = true
                binding.btnUpdate.visibility = View.INVISIBLE
                binding.btnTryAgainUpdate.visibility = View.VISIBLE
                binding.tvNewVersion.text = "-"
                binding.tvCurrentVersion.text = "-"
                Toast.makeText(BetApp.getAppContext(), BetApp.getAppContext().getString(R.string.error_conection_message), Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCancelUpdate.setOnClickListener { click ->
            findNavController().navigate(R.id.action_nav_update_to_nav_value_entry)
        }
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

    override fun onStart() {
        super.onStart()
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = null
    }
}