package com.devmin.android_review.data.http

import androidx.annotation.Keep
import com.devmin.android_review.entity.DataResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

@Keep
interface Api {
    @GET("App/json/{jsonKey}")
    fun getRead(@Path("jsonKey") key: String): Single<Response<DataResponse>>
}