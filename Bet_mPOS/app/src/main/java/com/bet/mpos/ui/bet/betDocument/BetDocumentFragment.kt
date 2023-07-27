package com.bet.mpos.ui.bet.betDocument

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentBetDocumentBinding
import com.bet.mpos.util.Mask

class BetDocumentFragment : Fragment() {

    private var _binding: FragmentBetDocumentBinding? = null

    private val binding get() = _binding!!

    private var etCPF: EditText? = null
    private var etPhone: EditText? = null

    companion object {
        fun newInstance() = BetDocumentFragment()
    }

    private lateinit var viewModel: BetDocumentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBetDocumentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BetDocumentViewModel::class.java)
        viewModel.start(requireArguments())

        observeViewModel()
        buttons()
    }

    private fun buttons() {
        etCPF = binding.etDocumentBet
        etCPF?.addTextChangedListener(Mask.mask("###.###.###-##", etCPF!!))
        etCPF?.requestFocus()

        //SHOW KEYBOARD
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        etPhone = binding.etPhoneBet
        etPhone?.addTextChangedListener(Mask.mask("(##)#####-####", etPhone!!))

        binding.btnConfirmBetDocument.setOnClickListener { click ->

            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etCPF?.getWindowToken(), 0)

            viewModel.confirmClick(findNavController(), etCPF?.text.toString(), etPhone?.text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            if(isLoading){
                binding.pbBetDocument.visibility = View.VISIBLE
                binding.containerBetDocument.visibility = View.GONE
                binding.btnConfirmBetDocument.visibility = View.GONE
            }else{
                binding.pbBetDocument.visibility = View.GONE
                binding.containerBetDocument.visibility = View.VISIBLE
                binding.btnConfirmBetDocument.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        super.onStop()

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etCPF?.getWindowToken(), 0)

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = null
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