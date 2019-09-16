package com.xdd.openweather.model

import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("datasetDescription") val datasetDescription : String,
    @SerializedName("location") val locationWeatherInfoList : List<LocationWeatherInfo>
)