package com.xdd.openweather.view

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.xdd.openweather.R
import com.xdd.openweather.model.TimedWeatherMap
import com.xdd.openweather.model.enumModel.WeatherElementEnum

@BindingAdapter("decideSelectDeselectDrawable")
fun ImageButton.decideSelectDeselectDrawable(toSelect: Boolean?) {
    setImageResource(if (toSelect == true) R.drawable.to_select_icon else R.drawable.to_deselect_icon)
}

@BindingAdapter("timedWeatherMap", "weatherElementEnum")
fun TextView.setupWeatherView(timedWeatherMap: TimedWeatherMap?, weatherElementEnum: WeatherElementEnum) {
    val parameter = timedWeatherMap?.get(weatherElementEnum)
    if (parameter == null) {
        visibility = View.GONE
    } else {
        text = weatherElementEnum.describeParameter(parameter)
        visibility = View.VISIBLE
    }
}