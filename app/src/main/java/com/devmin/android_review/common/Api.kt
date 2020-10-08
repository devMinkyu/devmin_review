package com.devmin.android_review.common

import androidx.annotation.Keep
import com.devmin.android_review.entity.DataResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

@Keep
interface Api {
    @GET
    fun get(
        @Url url: String,
        @QueryMap options: Map<String, Any>,
    ): Single<Response<DataResponse>>

    @POST
    fun post(
        @Url url: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Completable

    @PUT
    fun put(
        @Url url: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Completable

    @DELETE
    fun delete(@Url url: String): Completable
}