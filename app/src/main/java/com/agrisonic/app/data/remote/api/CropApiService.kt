package com.agrisonic.app.data.remote.api

import com.agrisonic.app.data.model.CropPredictionRequest
import com.agrisonic.app.data.model.CropPredictionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CropApiService {

    @POST("api/crop-prediction")
    suspend fun predictCrop(@Body request: CropPredictionRequest): Response<CropPredictionResponse>
}
