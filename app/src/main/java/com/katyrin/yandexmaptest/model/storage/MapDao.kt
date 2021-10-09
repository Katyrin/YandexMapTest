package com.katyrin.yandexmaptest.model.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katyrin.yandexmaptest.model.data.MyPointEntity

@Dao
interface MapDao {

    @Query("SELECT * FROM MyPointEntity")
    suspend fun getPointList(): List<MyPointEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putPoint(myPointEntity: MyPointEntity)
}