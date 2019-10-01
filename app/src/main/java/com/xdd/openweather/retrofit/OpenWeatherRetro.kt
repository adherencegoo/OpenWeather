package com.xdd.openweather.retrofit

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.xddlib.presentation.Lg
import com.google.gson.GsonBuilder
import com.xdd.openweather.AppExecutors
import com.xdd.openweather.R
import com.xdd.openweather.model.Forecast
import com.xdd.openweather.model.enumModel.IJsonEnum
import com.xdd.openweather.model.enumModel.LocationEnum
import com.xdd.openweather.model.enumModel.WeatherElementEnum
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.ref.WeakReference

class OpenWeatherRetro(context: Context) {
    companion object {
        private const val BASE_URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/"
    }

    private val authorizationKey = context.getString(R.string.authorizationKey)

    enum class Error(val code: Int) {
        NO_AUTHORIZATION(877);

        companion object {
            val codeMap = values().map { it.code to it }.toMap()
        }
    }

    private val refContext = WeakReference(context)

    private val interceptedHttpClient = OkHttpClient.Builder().addInterceptor { chain ->

        kotlin.runCatching {
            val auth = getAuthorization()?.takeIf { it.isNotEmpty() }
                ?: throw RuntimeException(Error.NO_AUTHORIZATION.name)
            val newRequest = chain.request().newBuilder()
                .addHeader(authorizationKey, auth)
                .build()
            chain.proceed(newRequest)
        }.getOrElse {
            Lg.e(it)
            Response.Builder()
                .code(Error.NO_AUTHORIZATION.code)
                .protocol(Protocol.HTTP_2)
                .message("Dummy response")
                .request(chain.request())
                .body(ResponseBody.create(MediaType.get("text/html; charset=utf-8"), ""))
                .build()
//            throw IOException("NO_AUTHORIZATION")
        }

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

    fun getAuthorization() = refContext.get()?.let {
        PreferenceManager.getDefaultSharedPreferences(it)
    }?.getString(authorizationKey, null)

    fun setAuthorization(auth: String?) {
        val preference = refContext.get()?.let {
            PreferenceManager.getDefaultSharedPreferences(it)
        } ?: return

        preference.edit()
            .putString(authorizationKey, auth)
            .apply()
    }
}