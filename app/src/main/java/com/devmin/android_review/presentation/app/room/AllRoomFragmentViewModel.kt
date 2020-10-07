package com.devmin.android_review.presentation.app.room

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.devmin.android_review.data.local.AndroidPrefUtilService
import com.devmin.android_review.domain.repository.RoomRepository
import com.devmin.android_review.entity.Room
import com.devmin.android_review.presentation.app.common.BaseViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AllRoomFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var roomRepository: RoomRepository

    @Inject
    lateinit var pref: AndroidPrefUtilService

    companion object {
        private var PAGE_KEY: Int = 1
    }

    val error = ObservableBoolean(false)
    val isEmpty = ObservableBoolean(true)

    var favoriteSet = mutableSetOf<String>()

    private val roomLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, Room>> =
        MutableLiveData<PageKeyedDataSource<Int, Room>>()

    var roomPagedList: LiveData<PagedList<Room>>? = null
    private lateinit var roomDataSourceFactory: RoomDataSourceFactory
    override fun initialize() {
        super.initialize()
        rxSubject()
        favoriteSet = pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet().toMutableSet()
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(20).build()

        roomDataSourceFactory = RoomDataSourceFactory()

        roomPagedList = LivePagedListBuilder(roomDataSourceFactory, pagedListConfig).build()
        isEmpty.set(roomPagedList?.value?.isEmpty()?:true)
    }

    private fun rxSubject() {}

    fun refresh() {
        roomDataSourceFactory.refresh()
    }

    fun create(room: Room) {
        favoriteSet = pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet().toMutableSet()
        favoriteSet.add("${room.id}")
        pref.putStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, favoriteSet).blockingAwait()
        val disposable = roomRepository.create(room, true).subscribeOn(Schedulers.newThread())
            .subscribe()
        addDisposable(disposable)
    }

    fun delete(room: Room) {
        favoriteSet = pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet().toMutableSet()
        favoriteSet.remove("${room.id}")
        pref.putStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, favoriteSet).blockingAwait()
        val disposable = roomRepository.delete(room, true)
            .subscribeOn(Schedulers.newThread())
            .subscribe()
        addDisposable(disposable)
    }

    fun end(room: Room) {
        roomRepository.roomSubject.onNext(room)
    }

    inner class RoomDataSourceFactory : DataSource.Factory<Int, Room>() {
        private lateinit var gameDataSource: RoomDataSource
        override fun create(): DataSource<Int, Room> {
            gameDataSource = RoomDataSource()

            roomLiveDataSource.postValue(gameDataSource)
            return gameDataSource
        }

        fun refresh() {
            PAGE_KEY = 1
            gameDataSource.invalidate()
        }

    }

    inner class RoomDataSource : PageKeyedDataSource<Int, Room>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Room>
        ) {
            PAGE_KEY = 1
            val disposable =
                roomRepository.read(PAGE_KEY)
                    .observeOn(Schedulers.newThread())
                    .subscribe({
                        it?.let { list ->
                            PAGE_KEY += 1
                            callback.onResult(list, null, PAGE_KEY)
                        }
                    }) {
                        Timber.e(it)
                    }
            addDisposable(disposable)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Room>) {
            val disposable =
                roomRepository.read(PAGE_KEY)
                    .observeOn(Schedulers.newThread())
                    .subscribe({
                        it?.let { list ->
                            val adjacentKey = if (params.key == 1) {
                                null
                            } else {
                                PAGE_KEY -= 1
                                PAGE_KEY
                            }
                            callback.onResult(list, adjacentKey)
                        }

                    }) {
                        Timber.e(it)
                    }
            addDisposable(disposable)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Room>) {
            val disposable =
                roomRepository.read(params.key)
                    .observeOn(Schedulers.newThread())
                    .subscribe({
                        it?.let { list ->
                            PAGE_KEY += 1
                            callback.onResult(list, PAGE_KEY)
                        }
                    }) {
                        Timber.e(it)
                    }
            addDisposable(disposable)
        }
    }
}

