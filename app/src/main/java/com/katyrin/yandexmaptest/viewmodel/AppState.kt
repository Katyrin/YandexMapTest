package com.katyrin.yandexmaptest.viewmodel

import com.katyrin.yandexmaptest.model.data.MyPoint

sealed class AppState {
    data class Error(val message: String?) : AppState()
    data class SuccessList(val myPoints: List<MyPoint>) : AppState()
    data class CachePoint(val myPoint: MyPoint) : AppState()
}
