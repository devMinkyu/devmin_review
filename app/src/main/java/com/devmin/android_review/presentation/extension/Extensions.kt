package com.devmin.android_review.presentation.extension

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.devmin.android_review.BuildConfig
import com.devmin.android_review.R
import com.devmin.android_review.entity.Result
import com.devmin.android_review.presentation.app.common.BaseFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs


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

fun Fragment.baseIntent(uri:String) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(uri)
    )
    intent.setPackage(BuildConfig.APPLICATION_ID)
    startActivity(intent)
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

fun RecyclerView.showBackToTopAnimation(backToTop: FloatingActionButton) {
    val animator = backToTop.animate().setDuration(250)
        .translationY(backToTop.height.toFloat() + 250f)
    animator.start()
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var showBackToTop = false
        var mNewState = 0
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (mNewState == 1) {
                if (dy > 0 && showBackToTop.not()) {
                    showBackToTop = true
                    val animator1 = backToTop.animate().setDuration(250)
                        .translationY(0f)
                    animator1.start()
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            this.mNewState = newState
            try {
                if (this@showBackToTopAnimation.canScrollVertically(-1).not()) {
                    showBackToTop = false
                    val animator1 = backToTop.animate().setDuration(250)
                        .translationY(backToTop.height.toFloat() + 250f)
                    animator1.start()
                }
            } catch (e: NullPointerException) {
                this@showBackToTopAnimation.scrollToPosition(0)
            }
        }
    })
}