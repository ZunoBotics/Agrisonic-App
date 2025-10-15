package com.agrisonic.app.data.model

import com.google.gson.annotations.SerializedName

data class MarketPriceResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<MarketPrice>
)

data class MarketPrice(
    @SerializedName("id")
    val id: String,

    @SerializedName("market_name")
    val marketName: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("item_name")
    val itemName: String,

    @SerializedName("unit")
    val unit: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("trend_direction")
    val trendDirection: String, // "up", "down", "stable"

    @SerializedName("percentage_change")
    val percentageChange: Double,

    @SerializedName("date_recorded")
    val dateRecorded: String,

    @SerializedName("source")
    val source: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean = true
)
