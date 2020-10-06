package com.devmin.android_review.presentation.app.common

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import java.lang.ref.WeakReference
import javax.inject.Inject


abstract class BaseFragment<E : BaseViewModel> : Fragment(), HasAndroidInjector {
    @Inject
    internal lateinit var viewModel: E

    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<Any>

    private var viewModelProvider: WeakReference<ViewModelProvider>? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (getViewModel() as BaseViewModel).also { baseViewModelVal ->
            if (!baseViewModelVal.isInitialized) {
                baseViewModelVal.initialize()
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return frameworkFragmentInjector
    }

    protected fun getViewModel(): E =
        viewModelProvider.let { vmpRef ->
            vmpRef?.get()?:getNewViewModelProvider()
        }.get(viewModel::class.java)

    private fun getNewViewModelProvider(): ViewModelProvider {
        val nonNullViewModelProviderVal = ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
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
    protected fun loadGlideCircleImage(appCompatImageView: AppCompatImageView, url:String) {
        Glide.with(appCompatImageView)
            .load(url)
            .apply(RequestOptions.circleCropTransform().circleCrop())
            .into(appCompatImageView)
    }
}
