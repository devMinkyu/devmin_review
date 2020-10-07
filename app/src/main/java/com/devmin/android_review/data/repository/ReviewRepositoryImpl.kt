package com.devmin.android_review.data.repository

import com.devmin.android_review.data.http.Api
import com.devmin.android_review.data.local.AppDatabase
import com.devmin.android_review.domain.repository.ReviewRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryImpl  @Inject constructor() : ReviewRepository {
    @Inject
    lateinit var db: AppDatabase
    @Inject
    lateinit var api:Api



}