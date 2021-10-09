package com.katyrin.yandexmaptest.model.datasource

import com.katyrin.yandexmaptest.model.data.MyPoint

interface LocalDataSource {
    suspend fun getPointList(): List<MyPoint>
    suspend fun putPoint(myPoint: MyPoint)
}