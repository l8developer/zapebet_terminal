package com.bet.mpos.ui.menu.report.viewPrint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentViewPrintBinding

class ViewPrintFragment: Fragment() {
    private var _binding: FragmentViewPrintBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(ViewPrintViewModel::class.java)

        _binding = FragmentViewPrintBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.start(requireArguments())

        observeViewModel(viewModel)
        buttons(viewModel)

        return root
    }

    private fun observeViewModel(viewModel: ViewPrintViewModel) {
        viewModel.image.observe(viewLifecycleOwner){ image ->
            binding.ivPrint.setImageBitmap(image)
        }
    }

    private fun buttons(viewModel: ViewPrintViewModel) {
        binding.btnPrint.setOnClickListener { click ->
            viewModel.clickPrint()
        }
    }

    override fun onStart() {
        super.onStart()
        hideToolbarLogo()
    }

    private fun hideToolbarLogo(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = null
    }
}