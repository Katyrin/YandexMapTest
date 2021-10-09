package com.katyrin.yandexmaptest.model.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.katyrin.yandexmaptest.model.data.MyPointEntity

@Database(entities = [MyPointEntity::class], version = 1, exportSchema = false)
abstract class PointDataBase: RoomDatabase() {
    abstract fun getMapDao(): MapDao
}