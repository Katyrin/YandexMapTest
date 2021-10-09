package com.katyrin.yandexmaptest.model.repository

import com.katyrin.yandexmaptest.model.data.MyPoint
import com.katyrin.yandexmaptest.model.datasource.LocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun getPointList(): List<MyPoint> =
        withContext(Dispatchers.IO) { localDataSource.getPointList() }

    override suspend fun putPoint(myPoint: MyPoint): Unit =
        withContext(Dispatchers.IO) { localDataSource.putPoint(myPoint) }
}