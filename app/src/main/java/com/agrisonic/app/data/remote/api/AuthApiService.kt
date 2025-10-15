package com.agrisonic.app.data.remote.api

import com.agrisonic.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {

    @POST("api/auth/signin")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("api/auth/verify-code")
    suspend fun verifyCode(@Body request: VerifyCodeRequest): Response<VerifyCodeResponse>

    @POST("api/auth/signout")
    suspend fun logout(): Response<ApiResponse>

    @GET("api/auth/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ApiResponse>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>

    @POST("api/auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ApiResponse>

    @POST("api/auth/send-verification-code")
    suspend fun sendVerificationCode(@Body request: SendVerificationCodeRequest): Response<ApiResponse>

    @POST("api/auth/verify-signup")
    suspend fun verifySignup(@Body request: VerifySignupRequest): Response<ApiResponse>
}
