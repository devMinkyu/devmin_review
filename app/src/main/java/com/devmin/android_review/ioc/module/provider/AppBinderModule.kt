@file:Suppress("unused")

package com.devmin.android_review.ioc.module.provider

import com.devmin.android_review.data.repository.ReviewRepositoryImpl
import com.devmin.android_review.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppBinderModule {
    @Singleton
    @Binds
    abstract fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository
}