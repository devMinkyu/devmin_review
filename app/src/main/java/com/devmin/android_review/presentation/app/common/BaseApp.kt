package com.devmin.android_review.presentation.app.common

import androidx.multidex.MultiDexApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseApp : MultiDexApplication(), HasAndroidInjector {
    @Inject
    lateinit var daggerAndroidInjector: DispatchingAndroidInjector<Any>

    private var needToInject: Boolean = true

    protected abstract fun applicationInjector(): AndroidInjector<out BaseApp>
    override fun onCreate() {
        super.onCreate()
        injectIfNecessary()
    }

    private fun injectIfNecessary() {
        if (needToInject) {
            synchronized(this) {
                if (needToInject) {
                    @Suppress("UNCHECKED_CAST")
                    val applicationInjector = applicationInjector() as AndroidInjector<BaseApp>
                    applicationInjector.inject(this)
                    check(!needToInject) { "The AndroidInjector returned from applicationInjector() did not inject the " + "DaggerApplication" }
                }
            }
        }
    }

    @Inject
    internal fun setInjected() {
        needToInject = false
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return daggerAndroidInjector
    }
}