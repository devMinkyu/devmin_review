package com.devmin.android_review.presentation.app.common

import android.content.Context
import androidx.lifecycle.ViewModel
import com.devmin.android_review.data.local.AndroidPrefUtilService
import dagger.Lazy
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

abstract class BaseViewModel : ViewModel() {
    @field:[Inject Named("Application")]
    lateinit var appCtx: Lazy<Context>

    @Inject
    lateinit var pref: AndroidPrefUtilService

    private val disposables = mutableListOf<Disposable?>()
    var isInitialized = false

    override fun onCleared() {
        super.onCleared()
        disposables.map {
            it?.dispose()
        }
        disposables.clear()
    }

    @Synchronized
    fun addDisposable(disposable: Disposable?) {
        disposables.add(disposable)
    }
    open fun initialize() {
        isInitialized = true
    }
}