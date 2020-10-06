@file:Suppress("unused")

package com.devmin.android_review.ioc.module.injector.activity.fragment

import com.devmin.android_review.presentation.app.main.module.AllReviewFragment
import com.devmin.android_review.presentation.app.main.module.FavoriteReviewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainFragmentInjectorModule {
    @ContributesAndroidInjector
    fun allReviewFragment(): AllReviewFragment

    @ContributesAndroidInjector
    fun favoriteReviewFragment(): FavoriteReviewFragment
}