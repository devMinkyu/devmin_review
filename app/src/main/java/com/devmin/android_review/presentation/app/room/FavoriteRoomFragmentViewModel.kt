package com.devmin.android_review.presentation.app.room

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devmin.android_review.data.local.AndroidPrefUtilService
import com.devmin.android_review.domain.repository.RoomRepository
import com.devmin.android_review.entity.Result
import com.devmin.android_review.entity.Room
import com.devmin.android_review.entity.Sort
import com.devmin.android_review.presentation.app.common.BaseViewModel
import com.devmin.android_review.presentation.event.Event
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoriteRoomFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var roomRepository: RoomRepository

    lateinit var favoritePagedLiveData: LiveData<PagedList<Room>>
    val filterLiveData = MutableLiveData<Event<Any>>()

    val sortType = ObservableField(Sort.LATEST)

    override fun initialize() {
        super.initialize()
        rxSubject()
        loadFavoritePaged()
    }
    private fun rxSubject() {}
    private fun loadFavoritePaged() {
        val factory = roomRepository.favoriteRead(sortType.get()?:Sort.LATEST)
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
        roomRepository.refreshSubject.onNext(room.id)
        val disposable = roomRepository.delete(room, true)
            .subscribeOn(Schedulers.newThread())
            .subscribe()
        addDisposable(disposable)
    }
    fun end(room: Room) {
        roomRepository.roomSubject.onNext(room)
    }

    fun sort(sort: Sort) {
        isResult.set(Result.LOADING)
        sortType.set(sort)
        loadFavoritePaged()
        filterLiveData.postValue(Event(Any()))
    }
    fun refresh() {
        isResult.set(Result.LOADING)
        loadFavoritePaged()
        filterLiveData.postValue(Event(Any()))
    }
}

