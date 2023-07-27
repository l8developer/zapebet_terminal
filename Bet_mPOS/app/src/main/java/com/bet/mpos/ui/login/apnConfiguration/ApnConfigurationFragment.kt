package com.bet.mpos.ui.login.apnConfiguration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentApnConfigurationBinding


class ApnConfigurationFragment : Fragment() {

    private var _binding: FragmentApnConfigurationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(ApnConfigurationViewModel::class.java)

        _binding = FragmentApnConfigurationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        buttons(viewModel)

        return root
    }

    private fun buttons(viewModel: ApnConfigurationViewModel) {
        var auth: String = "NONE"
        val dados = arrayOf("NONE", "PAP", "CHAP", "PAP or CHAP")
        val adapter = ArrayAdapter(BetApp.getAppContext(), com.bet.mpos.R.layout.layout_spinner_item, dados)

        binding.spnAuthTypeApn.setAdapter(adapter)

        binding.spnAuthTypeApn.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                auth = adapterView.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        })

        binding.btnConfirmarApn.setOnClickListener { click ->
            viewModel.clickConfirm(
                binding.etNameApn.text.toString(),
                binding.etHostApn.text.toString(),
                binding.etUserApn.text.toString(),
                binding.etPasswordApn.text.toString(),
                auth,
                findNavController()
            )
        }
    }

}