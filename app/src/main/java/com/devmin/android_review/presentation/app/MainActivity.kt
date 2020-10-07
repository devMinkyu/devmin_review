package com.devmin.android_review.presentation.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devmin.android_review.R
import com.devmin.android_review.presentation.app.common.BaseActivity
import com.devmin.android_review.presentation.app.review.AllReviewFragment
import com.devmin.android_review.presentation.app.review.FavoriteReviewFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : BaseActivity<MainActivityViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settingView()
    }
    private fun settingView() {
        reviewPager.adapter = ReviewPagerAdapter(supportFragmentManager)
        pagerTab.setupWithViewPager(reviewPager)
    }
    private fun showReviewEnd() {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.contentFrame, MainFragment(), null)
//            .commit()
    }

    inner class ReviewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        override fun getCount(): Int  = 2
        override fun getItem(i: Int): Fragment {
            return when (i) {
                1 -> FavoriteReviewFragment()
                else -> AllReviewFragment()
            }
        }
        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                1 -> resources.getString(R.string.mainReview)
                else -> resources.getString(R.string.favoriteReview)
            }
        }
    }

}