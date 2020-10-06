package com.devmin.android_review.presentation.extension

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devmin.android_review.R
import com.devmin.android_review.presentation.app.common.BaseFragment


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
