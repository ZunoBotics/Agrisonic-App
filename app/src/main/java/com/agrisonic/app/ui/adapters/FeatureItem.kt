package com.agrisonic.app.ui.adapters

import androidx.annotation.DrawableRes

data class FeatureItem(
    val title: String,
    @DrawableRes val iconRes: Int,
    val route: String
)
