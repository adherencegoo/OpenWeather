package com.xdd.openweather.viewmodel

import android.app.Application
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.xddlib.presentation.Lg
import com.xdd.openweather.BaseApp
import com.xdd.openweather.model.Forecast
import com.xdd.openweather.model.enumModel.IJsonEnum
import com.xdd.openweather.model.enumModel.LocationEnum
import com.xdd.openweather.model.enumModel.WeatherElementEnum
import com.xdd.openweather.retrofit.OpenWeatherRetro
import com.xdd.openweather.utils.DiffLiveData
import com.xdd.openweather.utils.open
import com.xdd.openweather.view.AuthorizationDialog
import com.xdd.openweather.view.EnumSelectorDialog
import com.xdd.openweather.view.ErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KClass

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val _liveLoading = DiffLiveData(MutableLiveData<Boolean>())

    val liveLoading: LiveData<Boolean>
        get() = _liveLoading

    private val _liveForecast = MutableLiveData<Forecast>()

    val liveForecast: LiveData<Forecast>
        get() = _liveForecast

    private val locationSelector = EnumSelector(LocationEnum.Companion)

    private val weatherSelector = EnumSelector(WeatherElementEnum.Companion)

    val enumSelectorMap = mapOf(
        LocationEnum::class to locationSelector,
        WeatherElementEnum::class to weatherSelector
    )

    fun updateForecasts(view: View) {
        val fragmentActivity = view.context as FragmentActivity

        _liveLoading.postValue(true)
        getApplication<BaseApp>().openWeatherRetro.apiService.getForecast(
            locations = locationSelector.getActualSelected(fragmentActivity),
            weatherElements = weatherSelector.getActualSelected(fragmentActivity)
        ).enqueue(object : Callback<Forecast> {
            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                Lg.e(t)//xdd

                ErrorDialog.newInstance(t)
                    .open(fragmentActivity.supportFragmentManager, ErrorDialog.TAG)
                _liveLoading.postValue(false)
            }

            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                if (OpenWeatherRetro.Error.codeMap[response.code()] == OpenWeatherRetro.Error.NO_AUTHORIZATION) {
                    AuthorizationDialog().open(
                        fragmentActivity.supportFragmentManager,
                        AuthorizationDialog.TAG
                    )
                } else if (response.body() == null) {
                    ErrorDialog.newInstance(RuntimeException("Null Forecast data"))
                        .open(fragmentActivity.supportFragmentManager, ErrorDialog.TAG)
                }

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
            EnumSelectorDialog.newInstance(kClass).open(manager, EnumSelectorDialog.TAG)
        }
    }
}