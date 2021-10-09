package com.katyrin.yandexmaptest.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyPoint(
    val latitude: Double,
    val longitude: Double,
    val address: String = ""
) : Parcelable
