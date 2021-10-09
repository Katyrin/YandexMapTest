package com.katyrin.yandexmaptest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.yandexmaptest.model.data.MyPoint
import com.katyrin.yandexmaptest.model.repository.Repository
import kotlinx.coroutines.*

class MapViewModel(
    private val repository: Repository
) : ViewModel() {

    private var myPoint: MyPoint? = null

    private var mutableLiveData: MutableLiveData<AppState> = MutableLiveData()
    val liveData: LiveData<AppState> = mutableLiveData

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main +
                SupervisorJob() +
                CoroutineExceptionHandler { _, throwable -> handlerError(throwable) }
    )

    private fun handlerError(throwable: Throwable) {
        mutableLiveData.value = AppState.Error(throwable.message)
    }

    fun cacheLocation(latitude: Double, longitude: Double) {
        myPoint = MyPoint(latitude, longitude)
    }

    fun saveCacheLocationWithAddress(address: String) {
        cancelJob()
        viewModelCoroutineScope.launch {
            val myPointWithAddress: MyPoint? = myPoint?.copy(address = address)
            myPointWithAddress?.let { repository.putPoint(it) }
            mutableLiveData.value = myPointWithAddress?.let { AppState.CachePoint(it) }
        }
    }

    fun getMyPointList() {
        cancelJob()
        viewModelCoroutineScope.launch {
            mutableLiveData.value = AppState.SuccessList(repository.getPointList())
        }
    }

    private fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    override fun onCleared() {
        cancelJob()
        myPoint = null
        super.onCleared()
    }
}