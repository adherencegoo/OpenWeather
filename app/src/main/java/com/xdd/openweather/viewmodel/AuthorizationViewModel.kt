package com.xdd.openweather.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.xdd.openweather.BaseApp

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {
    val authorizationValue = ObservableField<String>()

    fun setAuthorization() {
        getApplication<BaseApp>().openWeatherRetro.setAuthorization(authorizationValue.get())
    }
}