package com.devmin.android_review.presentation.app.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentMainBinding
import com.devmin.android_review.presentation.app.common.BaseFragment
import com.devmin.android_review.presentation.app.main.module.AllReviewFragment
import com.devmin.android_review.presentation.app.main.module.FavoriteReviewFragment
import com.devmin.android_review.presentation.extension.transactionFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainFragmentViewModel>() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.viewHandler = ViewHandler()
        binding.viewModel = getViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingTab()
        settingView()
    }

    private fun settingTab() {
        pagerTab.addTab(pagerTab.newTab().setText(resources.getString(R.string.mainReview)))
        pagerTab.addTab(pagerTab.newTab().setText(resources.getString(R.string.favoriteReview)))
        pagerTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { tabVal ->
                    getViewModel().settingReviewType(tabVal.position)
                }
            }
        })
    }

    private fun settingView() {
        childFragmentManager.findFragmentById(R.id.mainReview)?.let {
            it as AllReviewFragment
            transactionFragment(it, R.id.mainReview)
        } ?: transactionFragment(AllReviewFragment(), R.id.mainReview)

        childFragmentManager.findFragmentById(R.id.favoritesReview)?.let {
            it as FavoriteReviewFragment
            transactionFragment(it, R.id.favoritesReview)
        } ?: transactionFragment(FavoriteReviewFragment(), R.id.favoritesReview)
    }

    inner class ViewHandler {

    }
}