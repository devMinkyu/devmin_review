package com.devmin.android_review.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*

@Entity
@JsonClass(generateAdapter = true)
data class Room(
    @PrimaryKey var id:Int = -1,
    var name: String ="",
    @Embedded var description: Description? = null,
    var rate: Double = 0.0,
    var lastUpdate: Date? = null,
)