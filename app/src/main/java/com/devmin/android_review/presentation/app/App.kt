package com.devmin.android_review.presentation.app

import com.devmin.android_review.ioc.component.DaggerAppComponent
import com.devmin.android_review.presentation.app.common.BaseApp
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import timber.log.Timber

class App : BaseApp(){
    override fun applicationInjector(): AndroidInjector<App> = DaggerAppComponent
        .factory()
        .create(this)

    override fun onCreate() {
        super.onCreate()

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )

        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) + " : " + element.lineNumber
            }
        })
    }
}