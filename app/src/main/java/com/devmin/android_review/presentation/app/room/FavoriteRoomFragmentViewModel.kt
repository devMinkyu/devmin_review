package com.devmin.android_review.presentation.app.room

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devmin.android_review.data.local.AndroidPrefUtilService
import com.devmin.android_review.domain.repository.RoomRepository
import com.devmin.android_review.entity.Filter
import com.devmin.android_review.entity.Room
import com.devmin.android_review.entity.Sort
import com.devmin.android_review.presentation.app.common.BaseViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoriteRoomFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var roomRepository: RoomRepository

    @Inject
    lateinit var pref: AndroidPrefUtilService

    lateinit var favoritePagedLiveData: LiveData<PagedList<Room>>

    val isEmpty = ObservableBoolean(true)

    override fun initialize() {
        super.initialize()
        rxSubject()
        loadFavoritePaged()
    }
    private fun rxSubject() {}

    private fun refresh() {
        favoritePagedLiveData.value?.dataSource?.invalidate()
    }

    private fun loadFavoritePaged() {
        val factory = roomRepository.favoriteRead(Sort.DESC, Filter.RATE)
        val pagedListBuilder = LivePagedListBuilder(
            factory,
            20
        )
        favoritePagedLiveData = pagedListBuilder.build()
    }

    fun delete(room: Room) {
        val favoriteSet =
            pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet()
                .toMutableSet()
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
}

