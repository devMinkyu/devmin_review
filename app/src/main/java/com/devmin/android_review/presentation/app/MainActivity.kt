package com.devmin.android_review.presentation.app

import android.os.Bundle
import com.devmin.android_review.R
import com.devmin.android_review.presentation.app.common.BaseActivity
import com.devmin.android_review.presentation.app.main.MainFragment

class MainActivity : BaseActivity<MainActivityViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showHome()
    }
    private fun showHome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, MainFragment(), null)
            .commit()
    }
}