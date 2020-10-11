package com.devmin.android_review.ioc.module.provider

import com.devmin.android_review.common.Api
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Suppress("unused")
@Module
class NetworkProviderModule{
    @Provides
    @Singleton
    internal fun provideRemoteDataSource(
        client: OkHttpClient,
        moshi: Moshi
    ): Api {
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        val remoteClient = client.newBuilder()
            .retryOnConnectionFailure(true)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val baseUrl = "https://gccompany.co.kr/"

        val retrofit = Retrofit.Builder()
            .client(remoteClient)
            .baseUrl(baseUrl)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
            .build()

        return retrofit.create(Api::class.java)

    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory()).build()
    }

}