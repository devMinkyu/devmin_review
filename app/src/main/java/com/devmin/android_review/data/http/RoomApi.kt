package com.devmin.android_review.data.http

import com.devmin.android_review.common.Api
import com.devmin.android_review.entity.DataResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomApi @Inject constructor() {
    @Inject
    lateinit var api: Api

    fun read(key: Int): Single<DataResponse> {
        return api.get(
            url = "App/json/${key}.json",
            options = mapOf()
        ).flatMap {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    Single.just(response)
                } ?: Single.error(Throwable("인터넷 에러"))

            } else {
                Single.error(Throwable("인터넷 에러"))
            }
        }
    }
}