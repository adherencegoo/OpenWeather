package com.xdd.openweather.model

import com.google.gson.annotations.SerializedName

data class WeatherTime(
    @SerializedName("startTime") val startTime : String,
    @SerializedName("endTime") val endTime : String,
    @SerializedName("parameter") val weatherParameter : WeatherParameter
)