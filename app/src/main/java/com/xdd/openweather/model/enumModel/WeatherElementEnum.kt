package com.xdd.openweather.model.enumModel

@Suppress("unused")
enum class WeatherElementEnum : IJsonEnum {
    Wx, PoP, CI, MinT, MaxT;

    override val remoteName: String
        get() = toString()

    companion object : IJsonEnum.ICompanion<WeatherElementEnum> {
        override val nativeStringMap: Map<String, WeatherElementEnum>
            get() = enumList.map { it.toString() to it }.toMap()
        override val enumList: List<WeatherElementEnum>
            get() = values().toList()
    }
}