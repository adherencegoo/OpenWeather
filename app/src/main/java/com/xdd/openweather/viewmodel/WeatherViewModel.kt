package com.xdd.openweather.viewmodel

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xddlib.presentation.Lg
import com.xdd.openweather.model.Forecast
import com.xdd.openweather.model.enumModel.IJsonEnum
import com.xdd.openweather.model.enumModel.LocationEnum
import com.xdd.openweather.model.enumModel.WeatherElementEnum
import com.xdd.openweather.retrofit.OpenWeatherRetro
import com.xdd.openweather.utils.DiffLiveData
import com.xdd.openweather.view.EnumSelectorDialog
import com.xdd.openweather.view.ErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KClass

class WeatherViewModel : ViewModel() {
    private val _liveLoading = DiffLiveData(MutableLiveData<Boolean>())

    val liveLoading: LiveData<Boolean>
        get() = _liveLoading

    private val _liveForecast = MutableLiveData<Forecast>()

    val liveForecast: LiveData<Forecast>
        get() = _liveForecast

    val locationSelector = EnumSelector(LocationEnum.Companion)

    val weatherSelector = EnumSelector(WeatherElementEnum.Companion)

    val enumSelectorMap = mapOf(
        LocationEnum::class to locationSelector,
        WeatherElementEnum::class to weatherSelector
    )

    fun updateForecasts(
        limit: Int? = null,
        offset: Int? = null,
        locations: Collection<LocationEnum>? = null,
        weatherElements: Collection<WeatherElementEnum>? = null,
        failureHandler: ((Call<Forecast>, Throwable) -> Unit)? = null
    ) {
        _liveLoading.postValue(true)
        OpenWeatherRetro.apiService.getForecast(limit, offset, locations, weatherElements)
            .enqueue(object : Callback<Forecast> {
                override fun onFailure(call: Call<Forecast>, t: Throwable) {
                    Lg.e(t)//xdd
                    failureHandler?.invoke(call, t)
                    _liveLoading.postValue(false)
                }

                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                    _liveForecast.postValue(response.body())
                    _liveLoading.postValue(false)
                }
            })
    }

    fun openLocationSelectorDialog(view: View) = openEnumSelectorDialog(view, LocationEnum::class)

    fun openWeatherSelectorDialog(view: View) =
        openEnumSelectorDialog(view, WeatherElementEnum::class)

    private fun <T : IJsonEnum> openEnumSelectorDialog(view: View, kClass: KClass<T>) {
        (view.context as? FragmentActivity)?.supportFragmentManager?.let { manager ->
            val transaction = manager.beginTransaction()

            // remove existing fragment if any
            manager.findFragmentByTag(EnumSelectorDialog.TAG)?.let(transaction::remove)

            // will also commit the transaction
            EnumSelectorDialog.newInstance(kClass).show(transaction, EnumSelectorDialog.TAG)
        }
    }

    fun openErrorDialog(manager: FragmentManager, throwable: Throwable) {
        val transaction = manager.beginTransaction()

        manager.findFragmentByTag(ErrorDialog.TAG)?.let(transaction::remove)

        ErrorDialog.newInstance(throwable).show(transaction, ErrorDialog.TAG)
    }
}