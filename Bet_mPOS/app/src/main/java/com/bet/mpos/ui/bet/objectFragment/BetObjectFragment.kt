package com.bet.mpos.ui.bet.objectFragment

import android.content.res.Resources.Theme
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

    private var activeBtn = 0;
    //private var activateBtn = 1;

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
        changeScrollViewPosition(binding.hsvBtn1)
        activateButton(1)
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

    private fun activateButton(activateBtn: Int)
    {
        when(activeBtn)
        {
            1 -> {
                binding.hsvBtn1.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn1.setBackgroundResource(R.drawable.border_outline_black)
            }
            2 -> {
                binding.hsvBtn2.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn2.setBackgroundResource(R.drawable.border_outline_black)
            }
            3 -> {
                binding.hsvBtn3.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn3.setBackgroundResource(R.drawable.border_outline_black)
            }
            4 -> {
                binding.hsvBtn4.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn4.setBackgroundResource(R.drawable.border_outline_black)
            }
            5 -> {
                binding.hsvBtn5.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn5.setBackgroundResource(R.drawable.border_outline_black)
            }
            6 -> {
                binding.hsvBtn6.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn6.setBackgroundResource(R.drawable.border_outline_black)
            }
            7 -> {
                binding.hsvBtn7.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn7.setBackgroundResource(R.drawable.border_outline_black)
            }
            8 -> {
                binding.hsvBtn8.setTextColor(resources.getColor(R.color.black))
                binding.hsvBtn8.setBackgroundResource(R.drawable.border_outline_black)
            }
        }

        when(activateBtn)
        {
            1 -> {
                binding.hsvBtn1.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn1.setBackgroundResource(R.drawable.border_primary)
            }
            2 -> {
                binding.hsvBtn2.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn2.setBackgroundResource(R.drawable.border_primary)
            }
            3 -> {
                binding.hsvBtn3.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn3.setBackgroundResource(R.drawable.border_primary)
            }
            4 -> {
                binding.hsvBtn4.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn4.setBackgroundResource(R.drawable.border_primary)
            }
            5 -> {
                binding.hsvBtn5.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn5.setBackgroundResource(R.drawable.border_primary)
            }
            6 -> {
                binding.hsvBtn6.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn6.setBackgroundResource(R.drawable.border_primary)
            }
            7 -> {
                binding.hsvBtn7.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn7.setBackgroundResource(R.drawable.border_primary)
            }
            8 -> {
                binding.hsvBtn8.setTextColor(resources.getColor(R.color.white))
                binding.hsvBtn8.setBackgroundResource(R.drawable.border_primary)
            }
        }

        activeBtn = activateBtn
    }

    private fun buttons() {


        binding.hsvBtn1.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn1)
            viewModel.loadLeaguesFromCountry("Brazil")
            //binding.hsvBtn1.setTextColor(resources.getColor(R.color.white))
            activateButton(1)
        }
        binding.hsvBtn2.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn2)
            viewModel.loadLeaguesFromCountry("England")
            //activateBtn = 2
            activateButton(2)
        }
        binding.hsvBtn3.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn3)
            viewModel.loadLeaguesFromCountry("Spain")
            activateButton(3)
        }
        binding.hsvBtn4.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn4)
            viewModel.loadLeaguesFromCountry("World")
            activateButton(4)
        }
        binding.hsvBtn5.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn5)
            viewModel.loadLeaguesFromCountry("Italy")
            activateButton(5)
        }
        binding.hsvBtn6.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn6)
            viewModel.loadLeaguesFromCountry("Germany")
            activateButton(6)
        }
        binding.hsvBtn7.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn7)
            viewModel.loadLeaguesFromCountry("France")
            activateButton(7)
        }
        binding.hsvBtn8.setOnClickListener { click ->
            changeScrollViewPosition(binding.hsvBtn8)
            viewModel.loadLeaguesFromCountry("USA")
            activateButton(8)
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