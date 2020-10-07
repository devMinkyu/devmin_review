package com.devmin.android_review.data.local

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("unused")
@Singleton
class AndroidPrefUtilService @Inject constructor() {
    @Inject
    lateinit var sp: SharedPreferences

    fun hasKey(key: Key): Single<Boolean> {
        return Single.just(sp.contains(key.name))
    }

    fun remove(key: Key): Completable {
        return Completable.fromAction { sp.edit().remove(key.name).apply() }
    }

    fun clearAll(): Completable {
        return Completable.fromAction {
            sp.edit().clear().apply()
        }
    }

    fun getInt(key: Key): Single<Int> {
        return Single.just(sp.getInt(key.name, -1))
    }

    fun getInt(key: Key, defaultVal: Int): Single<Int> {
        return Single.just(sp.getInt(key.name, defaultVal))
    }

    fun getString(key: Key): Single<String> {
        return Single.just(sp.getString(key.name, "")!!)
    }

    fun getString(key: Key, defaultVal: String): Single<String> {
        return Single.just(sp.getString(key.name, defaultVal)!!)
    }

    fun getStringSet(key: Key, defaultVal: Set<String>): Single<Set<String>> {
        return Single.just(sp.getStringSet(key.name, defaultVal)!!)
    }

    fun getBool(key: Key): Single<Boolean> {
        return getBool(key, false)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getBool(key: Key, defaultVal: Boolean): Single<Boolean> {
        return Single.just(sp.getBoolean(key.name, defaultVal))
    }

    fun getFloat(key: Key): Single<Float> {
        return Single.just(sp.getFloat(key.name, 0f))
    }

    fun getLong(key: Key, value: Long): Single<Long> {
        return Single.just(sp.getLong(key.name, value))
    }

    fun putInt(key: Key, value: Int): Completable {
        return Completable.fromAction { sp.edit().putInt(key.name, value).apply() }
    }

    fun putString(key: Key, value: String): Completable {
        return Completable.fromAction { sp.edit().putString(key.name, value).apply() }
    }

    fun putStringSet(key: Key, value: Set<String>): Completable {
        return Completable.fromAction { sp.edit().putStringSet(key.name, value).apply() }
    }

    fun putBool(key: Key, value: Boolean): Completable {
        return Completable.fromAction { sp.edit().putBoolean(key.name, value).apply() }
    }

    fun putFloat(key: Key, value: Float): Completable {
        return Completable.fromAction { sp.edit().putFloat(key.name, value).apply() }
    }

    fun putLong(key: Key, value: Long): Completable {
        return Completable.fromAction { sp.edit().putLong(key.name, value).apply() }
    }

    enum class Key {
        FAVORITE_ID
    }
}