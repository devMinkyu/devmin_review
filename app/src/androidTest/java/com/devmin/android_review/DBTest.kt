package com.devmin.android_review

import androidx.paging.LivePagedListBuilder
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.devmin.android_review.common.AppDatabase
import com.devmin.android_review.data.local.FavoritesDao
import com.devmin.android_review.entity.Sort
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DBTest {
    lateinit var db: AppDatabase
    lateinit var favoritesDao: FavoritesDao

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        favoritesDao = db.favoritesDao()
    }

    @Test
    fun addFavorite() {
        val newList = mutableListOf<com.devmin.android_review.entity.Room>()
        for (i in 0 until 10) {
            newList.add(com.devmin.android_review.entity.Room(
                id = i,
                name = "$i 번 방",
                thumbnail = "123",
                rate = 9.5,
                lastUpdate = Date()
            ))
        }
        favoritesDao.insertRoom(*newList.toTypedArray()).subscribe()

        val list = favoritesDao.getRoomsTest()
        Assert.assertEquals(newList.size, list.size)
    }
}