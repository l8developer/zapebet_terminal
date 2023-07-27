package com.bet.mpos.ui.login.homeLogin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentHomeLoginBinding
import com.bet.mpos.util.Functions

class HomeLoginFragment : Fragment() {

    private var _binding: FragmentHomeLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(HomeLoginViewModel::class.java)

        _binding = FragmentHomeLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        buttons(viewModel)

        return root
    }

    private fun buttons(viewModel: HomeLoginViewModel) {
        binding.btnHabilitar.setOnClickListener { click ->
            if(Functions.isConnected())
                findNavController().navigate(R.id.action_homeLoginFragment_to_enableFragment)
            else
                Toast.makeText(activity, activity?.getString(R.string.error_conection_message), Toast.LENGTH_SHORT).show()
        }

        binding.btnNetworkConfig.setOnClickListener { click ->
            findNavController().navigate(R.id.action_homeLoginFragment_to_networkFragment)
        }
    }

}