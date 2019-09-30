package com.xdd.openweather

import android.app.Application
import com.xdd.openweather.retrofit.OpenWeatherRetro

class BaseApp : Application() {
    val openWeatherRetro: OpenWeatherRetro by lazy {
        OpenWeatherRetro(this)
    }
}