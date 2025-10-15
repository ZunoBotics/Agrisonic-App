package com.agrisonic.app.data.remote.api

import com.agrisonic.app.data.model.MarketPriceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketApiService {

    @GET("api/market-trends")
    suspend fun getMarketPrices(
        @Query("market") market: String? = null,
        @Query("crop") crop: String? = null,
        @Query("limit") limit: Int? = null
    ): Response<MarketPriceResponse>
}
