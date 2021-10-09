package com.katyrin.yandexmaptest.model.repository

import com.katyrin.yandexmaptest.model.data.MyPoint

interface Repository {
    suspend fun getPointList(): List<MyPoint>
    suspend fun putPoint(myPoint: MyPoint)
}