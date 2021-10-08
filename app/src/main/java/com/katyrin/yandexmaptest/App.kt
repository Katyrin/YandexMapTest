package com.katyrin.yandexmaptest

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    override fun onCreate() {
        MapKitFactory.setApiKey(getString(R.string.yandex_map_key))
        super.onCreate()
    }
}