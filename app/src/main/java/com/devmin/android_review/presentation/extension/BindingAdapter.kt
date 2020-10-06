package com.devmin.android_review.presentation.extension

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["bgCornerRadius"])
fun setBackgroundCornerRadius(view: View?, cornerRadius: Float) {
    var mCornerRadius = cornerRadius
    view?.let { mView ->
        val drawable = mView.background
        mCornerRadius *= mView.resources.displayMetrics.density
        val gradientDrawable = GradientDrawableUtil.getGradientDrawable(drawable, mCornerRadius)
        mView.background = gradientDrawable
    } ?: return
}