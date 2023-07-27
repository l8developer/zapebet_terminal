package com.bet.mpos.ui.menu.ajust

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentAjustsBinding

class AjustsFragment : Fragment() {

    private var _binding: FragmentAjustsBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AjustsFragment()
    }

    private lateinit var viewModel: AjustsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAjustsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AjustsViewModel::class.java)

        buttons()
    }

    private fun buttons() {
        binding.btnChangeEstablishment.setOnClickListener { click ->
            findNavController().navigate(R.id.action_nav_ajusts_to_changeEstablishmentFragment)
        }

        binding.btnUpdateConfigs.setOnClickListener { click ->
            findNavController().navigate(R.id.action_nav_ajusts_to_updateConfigsFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = null
    }

}