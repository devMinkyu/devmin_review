@file:Suppress("unused")

package com.devmin.android_review.ioc.module.injector.activity

import com.devmin.android_review.presentation.app.review.AllReviewFragment
import com.devmin.android_review.presentation.app.review.FavoriteReviewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityInjectorModule {
    @ContributesAndroidInjector
    fun allReviewFragment(): AllReviewFragment

    @ContributesAndroidInjector
    fun favoriteReviewFragment(): FavoriteReviewFragment
}