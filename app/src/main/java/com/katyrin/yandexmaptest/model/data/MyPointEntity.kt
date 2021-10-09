package com.katyrin.yandexmaptest.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyPointEntity(
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
