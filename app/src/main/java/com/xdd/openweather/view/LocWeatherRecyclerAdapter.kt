package com.xdd.openweather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xdd.openweather.databinding.ForecastRowBinding
import com.xdd.openweather.model.LocationWeatherInfo

class LocWeatherRecyclerAdapter(lifecycleOwner: LifecycleOwner) :
    AbstractRecyclerViewAdapter<ForecastRowBinding, LocationWeatherInfo>(lifecycleOwner) {
    private val timedWeatherCellPool = RecyclerView.RecycledViewPool()

    override fun onBindData(binding: ForecastRowBinding, data: LocationWeatherInfo) {
        binding.locationWeather = data
        (binding.timedWeatherRecycler.adapter as? TimedWeatherAdapter)?.postRawWeatherElements(data.weatherList)
    }

    override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): ForecastRowBinding {
        val binding = ForecastRowBinding.inflate(inflater, parent, false)!!
        binding.timedWeatherRecycler.apply {
            setRecycledViewPool(timedWeatherCellPool)
            layoutManager = LinearLayoutManager(parent.context, RecyclerView.HORIZONTAL, false)
            mLifecycleOwner?.let {
                adapter = TimedWeatherAdapter(it)
            }
        }

        return binding
    }
}