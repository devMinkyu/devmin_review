package com.devmin.android_review.presentation.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devmin.android_review.R
import com.devmin.android_review.presentation.app.common.BaseActivity
import com.devmin.android_review.presentation.app.common.BaseFragment
import com.devmin.android_review.presentation.app.room.AllRoomFragment
import com.devmin.android_review.presentation.app.room.FavoriteRoomFragment
import com.devmin.android_review.presentation.app.room.RoomEndFragment
import com.devmin.android_review.presentation.extension.leftReplace
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainActivityViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent(intent)
        settingView()
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        intent = newIntent
        handleIntent(intent)
    }

    private fun settingView() {
        roomPager.adapter = ReviewPagerAdapter(supportFragmentManager)
        pagerTab.setupWithViewPager(roomPager)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            when (appLinkData?.pathSegments?.get(0)) {
                "room" -> {
                    when (appLinkData.lastPathSegment) {
                        "end" -> showLeftPage(RoomEndFragment())
                    }
                }
            }
        }
    }

    private fun showLeftPage(frag: BaseFragment<*>) {
        leftReplace(
            R.id.contentFrame,
            frag
        )
    }

    inner class ReviewPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = 2
        override fun getItem(i: Int): Fragment {
            return when (i) {
                1 -> FavoriteRoomFragment()
                else -> AllRoomFragment()
            }
        }
        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                1 -> resources.getString(R.string.mainRoom)
                else -> resources.getString(R.string.favoriteRoom)
            }
        }
    }

}