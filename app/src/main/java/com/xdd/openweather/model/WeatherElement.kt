package com.xdd.openweather.model

import com.google.gson.annotations.SerializedName
import com.xdd.openweather.model.enumModel.WeatherElementEnum

data class WeatherElement(
    @SerializedName("elementName") val name : WeatherElementEnum,
    @SerializedName("time") val weatherTimeList : List<WeatherTime>
)