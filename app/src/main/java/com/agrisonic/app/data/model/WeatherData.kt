package com.agrisonic.app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: WeatherData
)

data class WeatherData(
    @SerializedName("location")
    val location: String,

    @SerializedName("coordinates")
    val coordinates: Coordinates,

    @SerializedName("current")
    val current: CurrentWeather,

    @SerializedName("forecast")
    val forecast: List<ForecastDay>
)

data class Coordinates(
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double
)

data class CurrentWeather(
    @SerializedName("temp")
    val temp: String,

    @SerializedName("feelsLike")
    val feelsLike: String,

    @SerializedName("humidity")
    val humidity: String,

    @SerializedName("windSpeed")
    val windSpeed: String,

    @SerializedName("pressure")
    val pressure: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("uvIndex")
    val uvIndex: String? = null,

    @SerializedName("visibility")
    val visibility: String? = null
)

data class ForecastDay(
    @SerializedName("date")
    val date: String,

    @SerializedName("shortDate")
    val shortDate: String,

    @SerializedName("temp")
    val temp: String,

    @SerializedName("minTemp")
    val minTemp: String? = null,

    @SerializedName("maxTemp")
    val maxTemp: String? = null,

    @SerializedName("rainChance")
    val rainChance: String,

    @SerializedName("humidity")
    val humidity: String,

    @SerializedName("windSpeed")
    val windSpeed: String,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("description")
    val description: String
)
