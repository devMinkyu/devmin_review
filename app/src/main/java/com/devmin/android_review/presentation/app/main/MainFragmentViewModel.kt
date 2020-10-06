package com.devmin.android_review.presentation.app.main

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.devmin.android_review.entity.ReviewType
import com.devmin.android_review.presentation.app.common.BaseViewModel
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor() : BaseViewModel() {
    val reviewMode = ObservableField<ReviewType>()

    override fun initialize() {
        super.initialize()
        reviewMode.set(ReviewType.ALL)
    }
    fun settingReviewType(position: Int) {
        when(position) {
            0 -> reviewMode.set(ReviewType.ALL)
            1 -> reviewMode.set(ReviewType.FAVORITE)
            else -> reviewMode.set(ReviewType.ALL)
        }
    }
}

