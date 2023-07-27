package com.bet.mpos.ui.menu.receiptReprint

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentReceiptReprintBinding

class ReceiptReprintFragment: Fragment() {

    private var errorMessage: String = ""

    private var _binding: FragmentReceiptReprintBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this).get(ReceiptReprintViewModel::class.java)
        _binding = FragmentReceiptReprintBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(requireActivity())
        buttons(viewModel)

        return root
    }

    private fun buttons(viewModel: ReceiptReprintViewModel) {
        binding.btnReprintClient.setOnClickListener { click -> viewModel.reprint("client") }
        binding.btnReprintEstablishment.setOnClickListener { click -> viewModel.reprint("establishment") }
    }

    override fun onStart() {
        super.onStart()
        showToolbarLogo()
    }

    fun showToolbarLogo(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = activity?.getDrawable(R.drawable.ic_logo_small_white)
        toolbar?.foregroundGravity = Gravity.CENTER
        toolbar?.visibility = View.VISIBLE
    }
}