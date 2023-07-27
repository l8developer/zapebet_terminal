package com.bet.mpos.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentHomeBinding
import com.bet.mpos.dialogs.ErrorNotificationDialog
import com.bet.mpos.objects.MenuType
import com.bet.mpos.ui.bet.objectFragment.BetObjectFragment
import com.bet.mpos.ui.sale.SaleFragment
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.terminal.PublicDevice
import com.zoop.sdk.api.terminal.System
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import java.lang.ref.WeakReference


class HomeFragment : Fragment() {

    private var configError: Boolean = false

    private var dialog: ErrorNotificationDialog? = null
    private var _binding: FragmentHomeBinding? = null
    private var navController: NavController? = null

    private val binding get() = _binding!!

    private lateinit var fragmentManager: FragmentManager
    private lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        dialog = ErrorNotificationDialog(this.requireActivity())

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //viewModel.menuOptions()
        observeViewModel(viewModel)
        buttons(viewModel)

        fragmentManager = childFragmentManager
        transaction = fragmentManager.beginTransaction()
        binding.layoutHomeOptions.visibility = View.GONE

        val fragment = BetObjectFragment()
        transaction.replace(R.id.container, fragment)

        transaction.commit()

        return root
    }

    private fun enableKey(state: Boolean) {
        SmartPOSPlugin.createPublicDeviceRequestBuilder()
            .callback(object : Callback<WeakReference<PublicDevice>>() {
                override fun onFail(error: Throwable) {
                    Log.e("onFail", error.message.toString())
                }

                override fun onSuccess(response: WeakReference<PublicDevice>) {
                    response.get()?.setNavigationKeyEnabled(System.NavigationKey.HOME, state)
                    response.get()?.setNavigationKeyEnabled(System.NavigationKey.RECENT, state)
                    response.get()?.setNavigationKeyEnabled(System.NavigationKey.BACK, true)
                }

            }).build().run { Zoop.post(this) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(requireView())
    }

    private fun observeViewModel(viewModel: HomeViewModel){

//        fragmentManager = childFragmentManager
//        transaction = fragmentManager.beginTransaction()
//
//        viewModel.unique.observe(viewLifecycleOwner){ type ->
//
////            when(type){
////                MenuType.SALE -> {
////                    binding.layoutHomeOptions.visibility = View.GONE
////
////                    val fragment = SaleFragment()
////                    transaction.replace(R.id.container, fragment)
////
////                    transaction.commit()
////                }
////                MenuType.SERVICE -> {
////                    binding.layoutHomeOptions.visibility = View.GONE
////
////                    val fragment = ServiceFragment()
////                    transaction.replace(R.id.container, fragment)
////
////                    transaction.commit()
////                }
////                MenuType.BET -> {
////                    binding.layoutHomeOptions.visibility = View.GONE
////
////                    val fragment = ProductObjectFragment()
////                    transaction.replace(R.id.container, fragment)
////
////                    transaction.commit()
////                }
////            }
//        }
//
//        viewModel.sale.observe(viewLifecycleOwner){ isSale ->
//            if(isSale) {
////                binding.btnSale.visibility = View.VISIBLE
//
//                binding.layoutHomeOptions.visibility = View.GONE
//
//                val fragment = SaleFragment()
//                transaction.replace(R.id.container, fragment)
//
//                transaction.commit()
//            }
//        }
//
//        viewModel.bet.observe(viewLifecycleOwner){ isBet ->
////            binding.btnBet.visibility = View.VISIBLE
//            val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
//            val menu = navView.menu
//            val item = menu.getItem(3)
//            item.isVisible = isBet
//        }
//
//        viewModel.service.observe(viewLifecycleOwner){ isService ->
////                binding.btnService.visibility = View.VISIBLE
//            val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
//            val menu = navView.menu
//            val item = menu.getItem(4)
//            item.isVisible = isService
//        }


    }

    private fun buttons(viewModel: HomeViewModel){

        binding.btnSale.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_saleFragment)
        }

        binding.btnBet.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_productObjectFragment)
        }

//        binding.btnService.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_home_to_serviceFragment)
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        dialog?.hide()
    }

    fun showToolbarLogo(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = activity?.getDrawable(R.drawable.ic_logo_small)
        toolbar?.foregroundGravity = Gravity.CENTER
        toolbar?.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.foreground = null
    }

    override fun onStart() {
        super.onStart()
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        showToolbarLogo()
        enableKey(false)
        viewModel.authentication(requireActivity())



//        println("onStart")
    }

}