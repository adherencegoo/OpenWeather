package com.xdd.openweather.model

import com.xdd.openweather.model.enumModel.WeatherElementEnum
import kotlin.collections.HashMap

data class Timespan(val startTime: String, val endTime: String) {
    constructor(weatherTime: WeatherTime) : this(weatherTime.startTime, weatherTime.endTime)
}

typealias TimedWeatherMap = Map<WeatherElementEnum, WeatherParameter>
typealias TimedWeatherMutableMap = HashMap<WeatherElementEnum, WeatherParameter>

/**
 * Orig: 1st key: [WeatherElementEnum], 2nd key: ([WeatherTime.startTime], [WeatherTime.endTime])
 * @return two key order is swapped
 *  */
fun List<WeatherElement>.toTimedWeatherMap(): Map<Timespan, TimedWeatherMap> {
    val result = HashMap<Timespan, TimedWeatherMutableMap>()

    forEach { weatherElement ->
        weatherElement.weatherTimeList.forEach { weatherTime ->
            result.getOrPut(Timespan(weatherTime)) {
                TimedWeatherMutableMap()
            }[weatherElement.name] = weatherTime.weatherParameter
        }
    }

    return result
}