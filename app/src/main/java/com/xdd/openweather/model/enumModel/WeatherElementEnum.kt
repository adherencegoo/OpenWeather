package com.xdd.openweather.model.enumModel

@Suppress("unused")
enum class WeatherElementEnum : IJsonEnum {
    Wx, PoP, CI, MinT, MaxT;

    override val remoteName: String
        get() = toString()

    companion object : IJsonEnum.ICompanion<WeatherElementEnum> {
        override val jsonTypeAdapter = object : IJsonEnum.JsonAdapter<WeatherElementEnum>() {
            override val enums: Array<WeatherElementEnum>
                get() = values()
        }
    }
}