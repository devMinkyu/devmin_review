package com.devmin.android_review.presentation.extension

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
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

@BindingAdapter("layout_constraintDimensionRatio")
fun setConstraintDimensionRatio(view: View, ratio: String?) {
    if (view.parent is ConstraintLayout) {
        val layoutParams =
            view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.dimensionRatio = ratio
        view.layoutParams = layoutParams
    }
}