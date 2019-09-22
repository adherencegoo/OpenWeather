package com.xdd.openweather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.xdd.openweather.databinding.ForecastRowBinding
import com.xdd.openweather.model.LocationWeatherInfo

class LocWeatherRecyclerAdapter(lifecycleOwner: LifecycleOwner) :
    AbstractRecyclerViewAdapter<ForecastRowBinding, LocationWeatherInfo>(lifecycleOwner) {
    override fun onBindData(binding: ForecastRowBinding, data: LocationWeatherInfo) {
        binding.locationWeather = data
    }

    override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ForecastRowBinding =
        ForecastRowBinding.inflate(inflater, parent, false)
}