package com.agrisonic.app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.agrisonic.app.data.local.Converters
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("farm_size")
    val farmSize: Double? = null,

    @SerializedName("crop_types")
    val cropTypes: List<String>? = null,

    @SerializedName("profile_picture_url")
    val profilePictureUrl: String? = null,

    @SerializedName("is_verified")
    val isVerified: Boolean = false,

    @SerializedName("is_admin")
    val isAdmin: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
) : Parcelable
