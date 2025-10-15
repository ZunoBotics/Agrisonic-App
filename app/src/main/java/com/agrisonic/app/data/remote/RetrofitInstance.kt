package com.agrisonic.app.data.remote

import com.agrisonic.app.BuildConfig
import com.agrisonic.app.data.local.PreferencesManager
import com.agrisonic.app.data.remote.api.*
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private lateinit var preferencesManager: PreferencesManager

    fun initialize(preferencesManager: PreferencesManager) {
        this.preferencesManager = preferencesManager
    }

    // Cookie Jar to store session cookies
    private val cookieJar = object : CookieJar {
        private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            val host = url.host
            cookieStore[host] = cookies.toMutableList()

            // Save session token to preferences
            cookies.find { it.name == "session-token" }?.let { cookie ->
                preferencesManager.saveSessionToken(cookie.value)
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val host = url.host
            return cookieStore[host] ?: emptyList()
        }
    }

    // Logging interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Auth interceptor to add session token
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = preferencesManager.getSessionToken()

        val request = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Cookie", "session-token=$token")
                .build()
        } else {
            originalRequest
        }

        chain.proceed(request)
    }

    // OkHttp client
    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API Services
    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val weatherApi: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    val cropApi: CropApiService by lazy {
        retrofit.create(CropApiService::class.java)
    }

    val marketApi: MarketApiService by lazy {
        retrofit.create(MarketApiService::class.java)
    }
}
