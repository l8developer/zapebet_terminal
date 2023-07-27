package com.bet.mpos.ui.menu.ajust.updateConfigs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.bet.mpos.R
import com.bet.mpos.databinding.FragmentUpdateConfigsBinding

class UpdateConfigsFragment : Fragment() {

    private var _binding: FragmentUpdateConfigsBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = UpdateConfigsFragment()
    }

    private lateinit var viewModel: UpdateConfigsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateConfigsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateConfigsViewModel::class.java)

        viewModel.bootData(findNavController())
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.userData.observe(viewLifecycleOwner){ data ->
            val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
            val fantasyName : TextView = navView.getHeaderView(0).findViewById(R.id.tv_fantasy_name)
            val corporateReason : TextView = navView.getHeaderView(0).findViewById(R.id.tv_corporate_reason)
            val document : TextView = navView.getHeaderView(0).findViewById(R.id.tv_document)

            if(data.socialName != null && data.socialName != "") {
                fantasyName.text = data.socialName

                val layoutSecond: LinearLayout = navView.getHeaderView(0).findViewById(R.id.layout_second)
                val layoutFantasyName: ConstraintLayout = navView.getHeaderView(0).findViewById(R.id.layout_fantasy_name)
//                val layoutDocument: ConstraintLayout = binding.navView.getHeaderView(0).findViewById(R.id.layout_fantasy_name)

                layoutFantasyName.visibility = View.VISIBLE
                layoutSecond.dividerPadding = 0
            }
            else{
                val layoutSecond: LinearLayout = navView.getHeaderView(0).findViewById(R.id.layout_second)
                val layoutFantasyName: ConstraintLayout = navView.getHeaderView(0).findViewById(R.id.layout_fantasy_name)
//                val layoutDocument: ConstraintLayout = binding.navView.getHeaderView(0).findViewById(R.id.layout_fantasy_name)

                layoutFantasyName.visibility = View.GONE
                layoutSecond.dividerPadding = 20
            }

            corporateReason.text = data.registerName
            document.text = data.documentValue
        }

        viewModel.userMessage.observe(viewLifecycleOwner){ message ->
            binding.tvUserMessageUpdateConfig.text = message
            binding.pbUpdateConfigs.visibility = View.GONE
        }
    }
}