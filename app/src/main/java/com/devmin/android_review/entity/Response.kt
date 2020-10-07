package com.devmin.android_review.entity


data class DataResponse(
    val msg: String = "",
    val data: RoomResponse? = null
)

data class RoomResponse(
    val totalCount: Int,
    val product: List<Room>
)
