package com.agrisonic.app.data.remote.api

import com.agrisonic.app.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("api/weather")
    suspend fun getWeather(
        @Query("location") location: String? = null,
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("type") type: String = "forecast", // "current" or "forecast"
        @Query("lang") lang: String = "en"
    ): Response<WeatherResponse>
}
