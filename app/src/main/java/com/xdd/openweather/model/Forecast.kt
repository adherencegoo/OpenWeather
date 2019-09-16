package com.xdd.openweather.model

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("success") val success : Boolean,
    @SerializedName("result") val result : Result,
    @SerializedName("records") val records : Records
)