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
import com.devmin.android_review.entity.Result
import com.devmin.android_review.entity.Room
import com.devmin.android_review.presentation.app.common.BaseViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AllRoomFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var roomRepository: RoomRepository

    companion object {
        private var PAGE_KEY: Int = 1
    }

    var favoriteSet = mutableSetOf<String>()
    val favoriteMap = mutableMapOf<Int, ObservableBoolean>()

    private val roomLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, Room>> =
        MutableLiveData<PageKeyedDataSource<Int, Room>>()

    var roomPagedList: LiveData<PagedList<Room>>? = null
    private lateinit var roomDataSourceFactory: RoomDataSourceFactory

    private val isComplete: () -> Unit = {
        pref.putStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, favoriteSet).blockingAwait()
    }
    private val isError: (Throwable) -> Unit = {
        Timber.d(it)
        favoriteSet =
            pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet()
                .toMutableSet()
    }

    override fun initialize() {
        super.initialize()
        rxSubject()
        favoriteSet =
            pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet()
                .toMutableSet()
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(20).build()

        roomDataSourceFactory = RoomDataSourceFactory()

        roomPagedList = LivePagedListBuilder(roomDataSourceFactory, pagedListConfig).build()
    }

    private fun rxSubject() {
        val disposable = roomRepository.refreshSubject
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                favoriteSet = pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet().toMutableSet()
                if(favoriteMap.containsKey(it)) {
                    favoriteMap[it]!!.set(favoriteSet.contains(it.toString()))
                }
            }) {
                Timber.e(it)
            }
        addDisposable(disposable)
    }

    fun refresh() {
        isResult.set(Result.LOADING)
        roomDataSourceFactory.refresh()
    }

    fun create(room: Room) {
        val disposable = updateRoom(room, false)
            .subscribe(isComplete, isError)
        addDisposable(disposable)
    }

    fun delete(room: Room) {
        val disposable = updateRoom(room, true)
            .subscribe(isComplete, isError)
        addDisposable(disposable)
    }

    private fun updateRoom(room: Room, isDelete: Boolean): Completable {
        favoriteSet =
            pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet()
                .toMutableSet()
        if (isDelete) {
            favoriteSet.remove("${room.id}")
        } else {
            favoriteSet.add("${room.id}")
        }

        return if (isDelete) {
            roomRepository.delete(room, true)
        } else {
            roomRepository.create(room, true)
        }.subscribeOn(Schedulers.newThread())
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
        private val isError: (Throwable) -> Unit = {
            isResult.set(Result.ERROR)
            Timber.e(it)
        }

        private fun remoteService(key: Int): Single<List<Room>> {
            return roomRepository.read(key)
                .observeOn(Schedulers.newThread())
        }

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Room>,
        ) {
            PAGE_KEY = 1
            val disposable =
                remoteService(PAGE_KEY)
                    .subscribe({
                        it?.let { list ->
                            PAGE_KEY += 1
                            callback.onResult(list, null, PAGE_KEY)
                        }
                    }, isError)
            addDisposable(disposable)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Room>) {
            val disposable =
                remoteService(params.key)
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

                    }, isError)
            addDisposable(disposable)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Room>) {
            val disposable =
                remoteService(params.key)
                    .subscribe({
                        it?.let { list ->
                            PAGE_KEY += 1
                            callback.onResult(list, PAGE_KEY)
                        }
                    }, isError)
            addDisposable(disposable)
        }
    }
}

