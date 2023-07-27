package com.bet.mpos.ui.bet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BetViewModel : ViewModel() {

    private val _categories = MutableLiveData<ArrayList<String>>().apply {}

    var categories: LiveData<ArrayList<String>> = _categories

    fun start() {
        loadCategories()
    }

    private fun loadCategories() {
        val categories = ArrayList<String>()
        categories.add("Brasileirão")
        categories.add("Em Breve")
        categories.add("Em Breve")
        categories.add("Em Breve")
        categories.add("Em Breve")

        _categories.value = categories
    }

    fun mountTabLayoutViewPager(
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        list: ArrayList<String>
    ) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = list[position]
        }.attach()
    }
}