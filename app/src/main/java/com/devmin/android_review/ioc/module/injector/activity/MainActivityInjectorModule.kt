@file:Suppress("unused")

package com.devmin.android_review.ioc.module.injector.activity

import com.devmin.android_review.presentation.app.room.AllRoomFragment
import com.devmin.android_review.presentation.app.room.FavoriteRoomFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityInjectorModule {
    @ContributesAndroidInjector
    fun allReviewFragment(): AllRoomFragment

    @ContributesAndroidInjector
    fun favoriteReviewFragment(): FavoriteRoomFragment
}