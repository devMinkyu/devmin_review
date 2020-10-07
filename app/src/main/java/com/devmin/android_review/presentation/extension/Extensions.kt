package com.devmin.android_review.presentation.extension

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.devmin.android_review.R
import com.devmin.android_review.presentation.app.common.BaseFragment


fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun AppCompatActivity.leftReplace(@IdRes id: Int, frag: BaseFragment<*>) {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .addToBackStack(null)
        .replace(id, frag, null).commit()
}

fun Fragment.transactionFragment(frag: BaseFragment<*>, @IdRes id: Int) {
    if (frag.isAdded) {
        childFragmentManager.beginTransaction().show(frag)
    } else {
        childFragmentManager.beginTransaction()
            .replace(id, frag, null)
            .commitNow()
    }
}

fun View.listAnimation() {
    val translateAnimation = TranslateAnimation(0f, 0f, 300f, 0f)
    val alphaAnimation: Animation = AlphaAnimation(0f, 1f)
    translateAnimation.duration = 500
    alphaAnimation.duration = 1300
    val animation = AnimationSet(true)
    animation.addAnimation(translateAnimation)
    animation.addAnimation(alphaAnimation)
    this.animation = animation
}

fun RecyclerView.ViewHolder.listAnimation(): RecyclerView.ViewHolder {
    this.itemView.listAnimation()
    return this
}
