package com.devmin.android_review.presentation.app.room

import com.devmin.android_review.entity.Room

interface RoomFavoriteViewHandler {
    fun like(room: Room)
    fun unlike(room: Room)
    fun roomEnd(room:Room)
}