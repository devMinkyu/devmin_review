@file:Suppress("unused")

package com.devmin.android_review.ioc.module.provider

import com.devmin.android_review.data.repository.RoomRepositoryImpl
import com.devmin.android_review.domain.repository.RoomRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppBinderModule {
    @Singleton
    @Binds
    abstract fun bindRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ): RoomRepository
}