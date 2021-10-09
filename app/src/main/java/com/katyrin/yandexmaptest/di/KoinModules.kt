package com.katyrin.yandexmaptest.di

import androidx.room.Room
import com.katyrin.yandexmaptest.model.datasource.LocalDataSource
import com.katyrin.yandexmaptest.model.datasource.LocalDataSourceImpl
import com.katyrin.yandexmaptest.model.mapping.PointMapping
import com.katyrin.yandexmaptest.model.mapping.PointMappingImpl
import com.katyrin.yandexmaptest.model.repository.Repository
import com.katyrin.yandexmaptest.model.repository.RepositoryImpl
import com.katyrin.yandexmaptest.model.storage.PointDataBase
import com.katyrin.yandexmaptest.viewmodel.MapViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {
    single { Room.databaseBuilder(get(), PointDataBase::class.java, "PointDataBase").build() }
    single { get<PointDataBase>().getMapDao() }
}

val modelModule: Module = module {
    single<PointMapping> { PointMappingImpl() }
    single<LocalDataSource> { LocalDataSourceImpl(mapDao = get(), pointMapping = get()) }
    single<Repository> { RepositoryImpl(localDataSource = get()) }
}

val mapModule: Module = module {
    factory { MapViewModel(repository = get()) }
}