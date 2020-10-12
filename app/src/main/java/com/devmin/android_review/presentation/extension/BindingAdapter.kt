package com.devmin.android_review.presentation.extension

import android.graphics.Typeface.*
import android.view.View
import android.widget.TextView
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
@BindingAdapter("text_style")
fun setTestStyle(view: View, isBold:Boolean) {
    if(view is TextView) {
        if (isBold) {
            view.setTypeface(DEFAULT_BOLD, BOLD)
        } else {
            view.setTypeface(DEFAULT, NORMAL)
        }
    }
}


@BindingAdapter(value = ["visible"])
fun setVisible(view: View?, visible: Boolean) {
    view?.let { mView ->
        if(visible) {
            mView.makeVisible()
        } else {
            mView.makeGone()
        }
    }
}