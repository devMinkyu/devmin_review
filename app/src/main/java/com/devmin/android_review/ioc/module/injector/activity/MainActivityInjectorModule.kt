@file:Suppress("unused")

package com.devmin.android_review.ioc.module.injector.activity

import com.devmin.android_review.ioc.module.injector.activity.fragment.MainFragmentInjectorModule
import com.devmin.android_review.presentation.app.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityInjectorModule {
    @ContributesAndroidInjector(
        modules = [
            MainFragmentInjectorModule::class
        ]
    )
    fun mainFragment(): MainFragment
}