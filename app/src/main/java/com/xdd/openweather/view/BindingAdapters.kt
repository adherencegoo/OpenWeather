package com.xdd.openweather.view

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.xdd.openweather.R

@BindingAdapter("decideSelectDeselectDrawable")
fun ImageButton.decideSelectDeselectDrawable(toSelect: Boolean?) {
    setImageResource(if (toSelect == true) R.drawable.to_select_icon else R.drawable.to_deselect_icon)
}