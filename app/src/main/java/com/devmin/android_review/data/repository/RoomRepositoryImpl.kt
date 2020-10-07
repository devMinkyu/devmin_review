package com.devmin.android_review.data.repository

import com.devmin.android_review.data.http.Api
import com.devmin.android_review.data.local.AppDatabase
import com.devmin.android_review.domain.repository.RoomRepository
import com.devmin.android_review.entity.Room
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepositoryImpl  @Inject constructor() : RoomRepository {
    @Inject
    lateinit var db: AppDatabase
    @Inject
    lateinit var api:Api

    override fun read(key: Int): Single<List<Room>> {
        return api.getRead("${key}.json").flatMap {
            if(it.isSuccessful) {
                Single.just(it.body()?.data?.product)
            } else {
                // todo local 처리 하든 이슈 처리
                Single.just(null)
            }
        }
    }
}