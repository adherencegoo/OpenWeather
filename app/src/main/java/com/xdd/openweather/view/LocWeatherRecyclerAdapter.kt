package com.xdd.openweather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.xdd.openweather.databinding.ForecastRowBinding
import com.xdd.openweather.model.LocationWeatherInfo

class LocWeatherRecyclerAdapter :
    AbstractRecyclerViewAdapter<ForecastRowBinding, LocationWeatherInfo>() {
    override fun onBindData(binding: ForecastRowBinding, data: LocationWeatherInfo) {
        binding.locationWeather = data
    }

    override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ForecastRowBinding =
        ForecastRowBinding.inflate(inflater, parent, false)
}