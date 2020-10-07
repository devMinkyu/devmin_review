package com.devmin.android_review.domain.repository

import androidx.paging.DataSource
import com.devmin.android_review.entity.Filter
import com.devmin.android_review.entity.Room
import com.devmin.android_review.entity.Sort
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Singleton

@Singleton
interface RoomRepository {
    companion object {
        private val refreshInstance: PublishSubject<Int> = PublishSubject.create()
        private val roomInstance: BehaviorSubject<Room> = BehaviorSubject.create()
    }

    val refreshSubject: PublishSubject<Int>
        get() = refreshInstance
    val roomSubject: BehaviorSubject<Room>
        get() = roomInstance

    fun create(room: Room, favorite: Boolean): Completable
    fun delete(room: Room, favorite: Boolean): Completable
    fun favoriteRead(sort:Sort, filter: Filter): DataSource.Factory<Int, Room>
    fun read(key: Int): Single<List<Room>>
}