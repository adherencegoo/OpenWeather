package com.xdd.openweather.model

import com.google.gson.annotations.SerializedName

data class WeatherParameter(
    @SerializedName("parameterName") val name : String,
    @SerializedName("parameterValue") val value : Int
)