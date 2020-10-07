package com.devmin.android_review.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Room(
    var id:Int = -1,
    var name: String ="",
    var description: Description? = null,
    var rate: Double = 0.0
)