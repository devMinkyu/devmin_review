package com.devmin.android_review.common

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.devmin.android_review.data.local.FavoritesDao
import com.devmin.android_review.entity.Room
import java.util.*


@Suppress("unused")
@Database(
    entities = [Room::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}

class DateTypeConverter {
    @TypeConverter
    fun toDate(value: Long?) = if (value == null) null else Date(value)

    @TypeConverter
    fun toLong(value: Date?) = value?.time ?: 0
}

