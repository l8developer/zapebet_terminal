package com.bet.mpos.ui.login.networkConfiguration

import android.content.Intent
import android.net.wifi.WifiManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentNetworkBinding

class NetworkFragment : Fragment() {

    private var _binding: FragmentNetworkBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(NetworkViewModel::class.java)

        _binding = FragmentNetworkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        buttons(viewModel)

        return root
    }

    private fun buttons(viewModel: NetworkViewModel) {
        binding.btnApn.setOnClickListener { click ->
            findNavController().navigate(R.id.action_networkFragment_to_apnConfigurationFragment)
        }

        binding.btnWifi.setOnClickListener { click ->
            startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))
        }
    }

    private fun hideLoading() {
        Handler().postDelayed(Runnable {
            binding.btnApn.visibility = View.VISIBLE
            binding.btnWifi.visibility = View.VISIBLE
            binding.pbNetworkConfig.visibility = View.GONE
        }, 500)
    }

    override fun onStart() {
        super.onStart()
        hideLoading()
    }
}