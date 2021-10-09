package com.katyrin.yandexmaptest.model.datasource

import com.katyrin.yandexmaptest.model.data.MyPoint
import com.katyrin.yandexmaptest.model.mapping.PointMapping
import com.katyrin.yandexmaptest.model.storage.MapDao

class LocalDataSourceImpl(
    private val mapDao: MapDao,
    private val pointMapping: PointMapping
) : LocalDataSource {

    override suspend fun getPointList(): List<MyPoint> =
        pointMapping.mapEntityListToMyPointList(mapDao.getPointList())

    override suspend fun putPoint(myPoint: MyPoint): Unit =
        mapDao.putPoint(pointMapping.mapMyPointToEntity(myPoint))
}