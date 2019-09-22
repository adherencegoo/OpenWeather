package com.xdd.openweather.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class DiffLiveData<T>(srcData: LiveData<T>) : MediatorLiveData<T>() {
    init {
        addSource(srcData) {
            if (value != it) {
                value = it
            }
        }
    }
}