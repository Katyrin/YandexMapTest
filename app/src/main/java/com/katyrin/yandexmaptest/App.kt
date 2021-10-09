package com.katyrin.yandexmaptest

import android.app.Application
import com.katyrin.yandexmaptest.di.databaseModule
import com.katyrin.yandexmaptest.di.mapModule
import com.katyrin.yandexmaptest.di.modelModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        MapKitFactory.setApiKey(getString(R.string.yandex_map_key))
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(mapModule, modelModule, databaseModule))
        }
    }
}