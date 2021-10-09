package com.katyrin.yandexmaptest.model.mapping

import com.katyrin.yandexmaptest.model.data.MyPoint
import com.katyrin.yandexmaptest.model.data.MyPointEntity

interface PointMapping {
    fun mapEntityListToMyPointList(myPointEntities: List<MyPointEntity>): List<MyPoint>
    fun mapEntityToMyPoint(myPointEntity: MyPointEntity): MyPoint
    fun mapMyPointToEntity(myPoint: MyPoint): MyPointEntity
}