package com.bet.mpos.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bet.mpos.ui.bet.objectFragment.BetObjectFragment
import java.util.ArrayList

class BetsCollectionAdapter(fragment: Fragment, list: ArrayList<String>): FragmentStateAdapter(fragment) {

    private val size = list.size
    private val list = list
    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {

        val fragment = BetObjectFragment()
        fragment.arguments = Bundle().apply {
            putString("id", list[position])
        }
        return fragment
    }

//    fun tabLayoutPosChanged(position: Int): Fragment {
//        val fragment = ProductObjectFragment()
//        fragment.arguments = Bundle().apply {
//            putInt("pos", position + 1)
//        }
//        return fragment
//    }
}