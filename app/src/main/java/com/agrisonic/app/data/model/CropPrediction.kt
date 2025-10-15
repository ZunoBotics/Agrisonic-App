package com.agrisonic.app.data.model

import com.google.gson.annotations.SerializedName

data class CropPredictionRequest(
    @SerializedName("nitrogen")
    val nitrogen: Double,

    @SerializedName("phosphorus")
    val phosphorus: Double,

    @SerializedName("potassium")
    val potassium: Double,

    @SerializedName("ph")
    val ph: Double,

    @SerializedName("ec")
    val ec: Double,

    @SerializedName("moisture")
    val moisture: Double
)

data class CropPredictionResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: CropPredictionData
)

data class CropPredictionData(
    @SerializedName("predictions")
    val predictions: List<CropPrediction>
)

data class CropPrediction(
    @SerializedName("crop_name")
    val cropName: String,

    @SerializedName("scientific_name")
    val scientificName: String? = null,

    @SerializedName("suitability_score")
    val suitabilityScore: Int,

    @SerializedName("crop_category")
    val cropCategory: String? = null,

    @SerializedName("match_details")
    val matchDetails: Map<String, MatchDetail>
)

data class MatchDetail(
    @SerializedName("status")
    val status: String, // "perfect", "partial", "poor"

    @SerializedName("score")
    val score: Double
)
