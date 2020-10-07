package com.devmin.android_review.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.devmin.android_review.entity.Room
import io.reactivex.rxjava3.core.Completable

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoom(vararg room: Room): Completable

    @Delete
    fun deleteRoom(vararg room: Room): Completable

    @Query("SELECT * FROM rooms Order By lastUpdate DESC")
    fun getRoomsOfDateDESC(): DataSource.Factory<Int, Room>

    @Query("SELECT * FROM rooms Order By lastUpdate ASC")
    fun getRoomsOfDateASC(): DataSource.Factory<Int, Room>

    @Query("SELECT * FROM rooms Order By rate DESC")
    fun getRoomsOfRateDESC(): DataSource.Factory<Int, Room>

    @Query("SELECT * FROM rooms Order By rate ASC")
    fun getRoomsOfRateASC(): DataSource.Factory<Int, Room>
}