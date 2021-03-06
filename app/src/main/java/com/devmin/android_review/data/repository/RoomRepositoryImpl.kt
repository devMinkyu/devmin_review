package com.devmin.android_review.data.repository

import androidx.paging.DataSource
import com.devmin.android_review.common.AppDatabase
import com.devmin.android_review.data.http.RoomApi
import com.devmin.android_review.domain.repository.RoomRepository
import com.devmin.android_review.entity.Room
import com.devmin.android_review.entity.Sort
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepositoryImpl  @Inject constructor() : RoomRepository {
    @Inject
    lateinit var db: AppDatabase
    @Inject
    lateinit var api: RoomApi

    override fun read(key: Int): Single<List<Room>> {
        return api.read(key).flatMap {
            it.data?.product?.let { list ->
                Single.just(list)
            }?:Single.just(listOf())
        }
    }

    override fun favoriteRead(sort: Sort): DataSource.Factory<Int, Room> {
        return when(sort) {
            Sort.RATE_DESC -> db.favoritesDao().getRoomsOfRateDESC()
            Sort.RATE_ASC -> db.favoritesDao().getRoomsOfRateASC()
            Sort.OLDEST -> db.favoritesDao().getRoomsOfOldest()
            Sort.LATEST -> db.favoritesDao().getRoomsOfLatest()
        }
    }

    override fun create(room: Room, favorite: Boolean): Completable {
        return if(favorite) {
            val newRoom = room.copy()
            newRoom.lastUpdate = Date()
            db.favoritesDao().insertRoom(newRoom)
        } else {
            // todo 서버
            Completable.complete()
        }
    }

    override fun delete(room: Room, favorite: Boolean): Completable {
        return if(favorite) {
            db.favoritesDao().deleteRoom(room)
        } else {
            // todo 서버
            Completable.complete()
        }
    }
}