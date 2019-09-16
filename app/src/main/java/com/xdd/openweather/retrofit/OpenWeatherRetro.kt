package com.xdd.openweather.retrofit

import com.google.gson.GsonBuilder
import com.xdd.openweather.AppExecutors
import com.xdd.openweather.model.Forecast
import com.xdd.openweather.model.enumModel.IJsonEnum
import com.xdd.openweather.model.enumModel.LocationEnum
import com.xdd.openweather.model.enumModel.WeatherElementEnum
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object OpenWeatherRetro {
    private const val AUTHORITY_KEY = "CWB-24EB0548-6442-49A6-AA1F-B515C6AF7ABA"
    private const val BASE_URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/"

    private val interceptedHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", AUTHORITY_KEY)
            .build()
        chain.proceed(newRequest)
    }.build()

    private val gson = GsonBuilder().also {
        IJsonEnum.decorateGsonBuilder(it)
    }.create()

    // Prevent: IllegalArgumentException: Parameter type must not include a type variable or wildcard: java.util.Collection<? extends com.xdd.openweather.model.enumModel.LocationEnum>
    @JvmSuppressWildcards
    interface ApiService {
        @GET("F-C0032-001")
        fun getForecast(
            @Query("limit") limit: Int? = null,
            @Query("offset") offset: Int? = null,
            @Query("locationName") locations: Collection<LocationEnum>? = null,
            @Query("elementName") weatherElements: Collection<WeatherElementEnum>? = null
        ): Call<Forecast>
    }

    val apiService: ApiService = Retrofit.Builder()
        .client(interceptedHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(IJsonEnum.converterFactory)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .callbackExecutor(AppExecutors.networkExecutor)
        .build()
        .create(ApiService::class.java)
}