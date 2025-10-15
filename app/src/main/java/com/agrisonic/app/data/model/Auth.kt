package com.agrisonic.app.data.model

import com.google.gson.annotations.SerializedName

// ============ Request Models ============

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class SignupRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String,

    @SerializedName("profilePictureUrl")
    val profilePictureUrl: String? = null
)

data class VerifyCodeRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("type")
    val type: String // "signup", "signin", "password_change"
)

data class ForgotPasswordRequest(
    @SerializedName("email")
    val email: String
)

data class ResetPasswordRequest(
    @SerializedName("token")
    val token: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String
)

data class ChangePasswordRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("newPassword")
    val newPassword: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String
)

data class SendVerificationCodeRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("type")
    val type: String // "signup" or "password_change"
)

data class VerifySignupRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("code")
    val code: String
)

// ============ Response Models ============

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: LoginData? = null,

    @SerializedName("error")
    val error: String? = null
)

data class LoginData(
    @SerializedName("user")
    val user: User,

    @SerializedName("redirect")
    val redirect: String? = null
)

data class SignupResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: SignupData? = null,

    @SerializedName("error")
    val error: String? = null
)

data class SignupData(
    @SerializedName("message")
    val message: String,

    @SerializedName("user")
    val user: User,

    @SerializedName("requiresVerification")
    val requiresVerification: Boolean
)

data class VerifyCodeResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: VerifyCodeData? = null,

    @SerializedName("error")
    val error: String? = null
)

data class VerifyCodeData(
    @SerializedName("message")
    val message: String
)

data class ApiResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: Any? = null,

    @SerializedName("error")
    val error: String? = null
)

data class UserResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: User? = null,

    @SerializedName("error")
    val error: String? = null
)
