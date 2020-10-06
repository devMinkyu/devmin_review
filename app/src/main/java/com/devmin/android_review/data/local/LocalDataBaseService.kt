package com.devmin.android_review.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Suppress("unused")
@Database(
    entities = [],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
