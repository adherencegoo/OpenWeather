package com.xdd.openweather.model.enumModel

import com.xdd.openweather.model.WeatherParameter

@Suppress("unused")
enum class WeatherElementEnum(
    override val localName: String,
    private val parameterDescriptor: (WeatherParameter) -> String
) : IJsonEnum {
    Wx("天氣現象", { it.name }),
    PoP("降雨機率", { "降雨機率:${it.name}%" }),
    CI("舒適度", { it.name }),
    MinT("最低溫", { it.name + "℃" }),
    MaxT("最高溫", { it.name + "℃" });

    override val remoteName: String
        get() = toString()

    fun describeParameter(parameter: WeatherParameter) = parameterDescriptor.invoke(parameter)

    companion object : IJsonEnum.ICompanion<WeatherElementEnum> {
        override val nativeStringMap: Map<String, WeatherElementEnum>
            get() = enumList.map { it.toString() to it }.toMap()
        override val enumList: List<WeatherElementEnum>
            get() = values().toList()
    }
}