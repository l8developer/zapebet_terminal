package com.bet.mpos.ui.bet.objectFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bet.mpos.R
import com.bet.mpos.adapters.AdapterProducts
import com.bet.mpos.databinding.FragmentBetObjectBinding
import com.google.android.material.tabs.TabLayout

class BetObjectFragment : Fragment() {

    private var _binding: FragmentBetObjectBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = BetObjectFragment()
    }

    private lateinit var viewModel: BetObjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBetObjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BetObjectViewModel::class.java)

        viewModel.start(findNavController(), requireActivity(), arguments, binding.tvLeague)

        observeViewModel()
        tabLayoutObserver()
        changeScrollViewPosition(binding.hsvBtn5)
        buttons()

    }

    private fun observeViewModel() {

        viewModel.list.observe(viewLifecycleOwner){ list ->
            val adapter = AdapterProducts(list, requireContext())

            viewModel.adapter(adapter, list)
            binding.rvProduct.layoutManager = LinearLayoutManager(requireContext())
            binding.rvProduct.adapter = adapter

        }

        viewModel.leagueList.observe(viewLifecycleOwner){ list ->
//            binding.tlProductObject.removeAllTabs()
//            list.forEach { name ->
//                binding.tlProductObject.addTab(binding.tlProductObject.newTab().setText(name))
//            }
        }

        viewModel.leagueIdList.observe(viewLifecycleOwner){ list ->
            val leagueList = viewModel.leagueList.value
            binding.tlProductObject.removeAllTabs()
            leagueList?.forEach { name ->
                binding.tlProductObject.addTab(binding.tlProductObject.newTab().setText(name))
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){ loading ->
            if(loading)
            {
                binding.rvProduct.visibility = View.GONE
                binding.pbGames.visibility = View.VISIBLE
            }
            else
            {
                binding.rvProduct.visibility = View.VISIBLE
                binding.pbGames.visibility = View.GONE
            }
        }
    }
    private fun tabLayoutObserver()
    {
        binding.tlProductObject.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d("tab change", "passou pelo tab change")
                viewModel.tabChanged(tab.position)
                Log.d("teste posicao", tab.position.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {
//                Log.d("tab change", "passou pelo tab change")
//                viewModel.tabChanged(tab.position)
            }
        })
    }

    private fun buttons() {


        binding.hsvBtn1.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn1)
            viewModel.loadLeaguesFromCountry("Brazil")
        }
        binding.hsvBtn2.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn2)
            viewModel.loadLeaguesFromCountry("England")
        }
        binding.hsvBtn3.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn3)
            viewModel.loadLeaguesFromCountry("Spain")
        }
        binding.hsvBtn4.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn4)
            viewModel.loadLeaguesFromCountry("World")
        }
        binding.hsvBtn5.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn5)
            viewModel.loadLeaguesFromCountry("Italy")
        }
        binding.hsvBtn6.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn6)
            viewModel.loadLeaguesFromCountry("Germany")
        }
        binding.hsvBtn7.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn7)
            viewModel.loadLeaguesFromCountry("France")
        }
        binding.hsvBtn8.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn8)
            viewModel.loadLeaguesFromCountry("USA")
        }

    }

    private fun changeScrollViewPosition(btn: Button) {
        val x: Int = btn.left
        val y: Int = btn.top
        binding.countriesScrollView.scrollTo(x - 5, y);
    }



    override fun onDestroyView() {
        super.onDestroyView()

//        viewModelShared.quant.removeObservers(viewLifecycleOwner)
//        viewModelShared.total.removeObservers(viewLifecycleOwner)
//
//        viewModel.quant.removeObservers(viewLifecycleOwner)
//        viewModel.total.removeObservers(viewLifecycleOwner)
//
//        requireActivity().viewModelStore.clear()
        viewModelStore.clear()

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