package com.devmin.android_review.presentation.app.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.lang.ref.WeakReference
import javax.inject.Inject

abstract class BaseActivity<E : BaseViewModel> : AppCompatActivity(), HasAndroidInjector {
    @Inject
    internal lateinit var viewModel: E

    @Inject
    lateinit var frameworkActivityInjector: DispatchingAndroidInjector<Any>

    private var viewModelProvider: WeakReference<ViewModelProvider>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

//        (getViewModel() as BaseViewModel).also { baseViewModelVal ->
//            if (!baseViewModelVal.isInitialized) {
//                baseViewModelVal.initialize()
//            }
//        }

        with(getViewModel() as BaseViewModel) {
            if(isInitialized.not()) {
                initialize()
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return frameworkActivityInjector
    }

    protected fun getViewModel(): E =
        viewModelProvider.let { vmpRef ->
            vmpRef?.get() ?: getNewViewModelProvider()
        }.get(viewModel::class.java)

    private fun getNewViewModelProvider(): ViewModelProvider {
        val nonNullViewModelProviderVal =
            ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    try {
                        @Suppress("UNCHECKED_CAST")
                        return viewModel as T
                    } catch (e: Exception) {
                        throw RuntimeException(e)
                    }
                }
            })
        viewModelProvider = WeakReference(nonNullViewModelProviderVal)
        return nonNullViewModelProviderVal
    }

}