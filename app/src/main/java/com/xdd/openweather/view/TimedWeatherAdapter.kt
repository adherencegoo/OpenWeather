package com.xdd.openweather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.xdd.openweather.databinding.TimedWeathersCellBinding
import com.xdd.openweather.model.Timespan
import com.xdd.openweather.model.TimedWeatherMap
import com.xdd.openweather.model.WeatherElement
import com.xdd.openweather.model.toTimedWeatherMap

class TimedWeatherAdapter(lifecycleOwner: LifecycleOwner) :
    AbstractRecyclerViewAdapter<TimedWeathersCellBinding, Pair<Timespan, TimedWeatherMap>>(
        lifecycleOwner
    ) {
    override fun onBindData(
        binding: TimedWeathersCellBinding,
        data: Pair<Timespan, TimedWeatherMap>
    ) {
        binding.timespan = data.first
        binding.weatherMap = data.second
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): TimedWeathersCellBinding = TimedWeathersCellBinding.inflate(inflater, parent, false)

    fun postRawWeatherElements(rawWeatherElements: List<WeatherElement>) {
        postData(rawWeatherElements.toTimedWeatherMap().toList().toMutableList())
    }
}