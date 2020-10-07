package com.devmin.android_review.domain.repository

import com.devmin.android_review.entity.Room
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface RoomRepository {
    fun read(key: Int): Single<List<Room>>
}