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

    val locationSelector = EnumSelector(LocationEnum.Companion)

    val weatherSelector = EnumSelector(WeatherElementEnum.Companion)

    val enumSelectorMap = mapOf(
        LocationEnum::class to locationSelector,
        WeatherElementEnum::class to weatherSelector
    )

    /**
     * @param errorCallback handle failure or wrong response
     * */
    fun updateForecasts(
        limit: Int? = null,
        offset: Int? = null,
        locations: Collection<LocationEnum>? = null,
        weatherElements: Collection<WeatherElementEnum>? = null,
        errorCallback: Callback<Forecast>? = null
    ) {
        _liveLoading.postValue(true)
        getApplication<BaseApp>().openWeatherRetro.apiService.getForecast(
            limit,
            offset,
            locations,
            weatherElements
        ).enqueue(object : Callback<Forecast> {
            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                Lg.e(t)//xdd
                errorCallback?.onFailure(call, t)
                _liveLoading.postValue(false)
            }

            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                errorCallback?.onResponse(call, response)
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