package com.devmin.android_review.ioc.module.injector

import com.devmin.android_review.ioc.module.injector.activity.MainActivityInjectorModule
import com.devmin.android_review.ioc.module.provider.AppProviderModule
import com.devmin.android_review.presentation.app.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [
        AppProviderModule::class
    ]
)
@Suppress("unused")
interface AppInjectorModule {
    @ContributesAndroidInjector(
        modules = [
            MainActivityInjectorModule::class
        ]
    )
    fun mainActivity(): MainActivity

}