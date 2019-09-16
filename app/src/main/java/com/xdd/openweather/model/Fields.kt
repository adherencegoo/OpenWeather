package com.xdd.openweather.model

import com.google.gson.annotations.SerializedName

data class Fields (
    @SerializedName("id") val id : String,
    @SerializedName("type") val type : String
)