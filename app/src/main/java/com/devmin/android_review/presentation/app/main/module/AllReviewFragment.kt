package com.devmin.android_review.presentation.app.main.module

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.devmin.android_review.R
import com.devmin.android_review.databinding.FragmentAllReviewBinding
import com.devmin.android_review.presentation.app.common.BaseFragment

class AllReviewFragment : BaseFragment<AllReviewFragmentViewModel>() {
    private lateinit var binding: FragmentAllReviewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_review, container, false)
        binding.viewHandler = ViewHandler()
        binding.viewModel = getViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    inner class ViewHandler {

    }
}