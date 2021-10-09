package com.katyrin.yandexmaptest.model.mapping

import com.katyrin.yandexmaptest.model.data.MyPoint
import com.katyrin.yandexmaptest.model.data.MyPointEntity

class PointMappingImpl : PointMapping {

    override fun mapEntityListToMyPointList(myPointEntities: List<MyPointEntity>): List<MyPoint> =
        mutableListOf<MyPoint>().apply {
            myPointEntities.forEach { myPointEntity -> add(mapEntityToMyPoint(myPointEntity)) }
        }

    override fun mapEntityToMyPoint(myPointEntity: MyPointEntity): MyPoint =
        with(myPointEntity) { MyPoint(latitude, longitude, address) }

    override fun mapMyPointToEntity(myPoint: MyPoint): MyPointEntity =
        with(myPoint) { MyPointEntity(latitude, longitude, address) }
}