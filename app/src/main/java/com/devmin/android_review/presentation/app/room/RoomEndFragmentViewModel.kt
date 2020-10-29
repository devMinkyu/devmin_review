package com.devmin.android_review.presentation.app.room

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.devmin.android_review.data.local.AndroidPrefUtilService
import com.devmin.android_review.domain.repository.RoomRepository
import com.devmin.android_review.entity.Room
import com.devmin.android_review.presentation.app.common.BaseViewModel
import com.devmin.android_review.presentation.event.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class RoomEndFragmentViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var roomRepository: RoomRepository

    val roomLiveDate = MutableLiveData<Event<Room>>()

    val title = ObservableField("")
    val description = ObservableField("")
    val rate = ObservableField(0.0)
    val favorite = ObservableBoolean(false)

    var favoriteSet = mutableSetOf<String>()

    var room: Room? = null
    override fun initialize() {
        super.initialize()
        favoriteSet =
            pref.getStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, setOf()).blockingGet()
                .toMutableSet()
        rxSubject()
    }

    private fun rxSubject() {
        val disposable = roomRepository.roomSubject
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                room = it
                favorite.set(favoriteSet.contains(it.id.toString()))
                title.set(it.name)
                rate.set(it.rate)
                description.set(it.description?.subject ?: "")
                roomLiveDate.postValue(Event(it))
            }) {
                Timber.e(it)
            }
        addDisposable(disposable)
    }

    private fun create(room: Room) {
        val disposable = updateRoom(room, false)
            .subscribe()
        addDisposable(disposable)
    }

    private fun delete(room: Room) {
        val disposable = updateRoom(room, true)
            .subscribe()
        addDisposable(disposable)
    }

    private fun updateRoom(room: Room, isDelete: Boolean): Completable {
        if (isDelete) {
            favoriteSet.remove("${room.id}")
        } else {
            favoriteSet.add("${room.id}")
        }
        pref.putStringSet(AndroidPrefUtilService.Key.FAVORITE_ID, favoriteSet).blockingAwait()
        roomRepository.refreshSubject.onNext(room.id)
        return if (isDelete) {
            roomRepository.delete(room, true)
        } else {
            roomRepository.create(room, true)
        }.subscribeOn(Schedulers.newThread())
    }

    fun like() {
        room?.let { room ->
            favorite.set(favorite.get().not())
            if (favorite.get()) {
                create(room)
            } else {
                delete(room)
            }
        }
    }
}

