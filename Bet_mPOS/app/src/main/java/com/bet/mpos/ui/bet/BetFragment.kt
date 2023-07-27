package com.bet.mpos.ui.bet

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bet.mpos.adapters.BetsCollectionAdapter
import com.bet.mpos.databinding.FragmentBetBinding

class BetFragment : Fragment(){

    private lateinit var betsCollectionAdapter: BetsCollectionAdapter

    private var _binding: FragmentBetBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = BetFragment()
    }

    private lateinit var viewModel: BetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BetViewModel::class.java)
        viewModel.start()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) { list ->

            betsCollectionAdapter = BetsCollectionAdapter(this, list)
            binding.vpContentProducts.adapter = betsCollectionAdapter
            viewModel.mountTabLayoutViewPager(binding.tlProducts, binding.vpContentProducts, list)
        }

//        val viewSharedModel = ViewModelProvider(requireActivity()).get(ProductObjectViewModel::class.java)
//        viewSharedModel.nextScreen.observe(viewLifecycleOwner){ isNext ->
//            if(isNext){
//                navigateToFragment(R.id.betDocumentFragment)
//            }
//                println("Next screen")
////                findNavController().navigate(R.id.action_betFragment_to_betDocumentFragment2)
//        }
    }

}