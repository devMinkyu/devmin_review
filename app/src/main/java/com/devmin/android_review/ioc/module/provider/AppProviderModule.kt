@file:Suppress("unused")

package com.devmin.android_review.ioc.module.provider

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.devmin.android_review.data.local.AppDatabase
import com.devmin.android_review.presentation.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes =
    [
        NetworkProviderModule::class, AppBinderModule::class
    ]
)
class AppProviderModule {
    @Provides
    @Singleton
    @Named("Application")
    internal fun provideApplicationContext(app: App): Context {
        return app.baseContext
    }

    @Provides
    internal fun providePreferenceManager(@Named("Application") context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    internal fun provideRes(@Named("Application") context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    internal fun provideAppDataBase(
        @Named("Application") context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "devmin-android-db"
        ).build()
    }
}