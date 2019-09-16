package com.xdd.openweather.model

import com.google.gson.annotations.SerializedName
import com.xdd.openweather.model.enumModel.LocationEnum

data class LocationWeatherInfo(
    @SerializedName("locationName") val locationEnum : LocationEnum,
    @SerializedName("weatherElement") val weatherList : List<WeatherElement>
)