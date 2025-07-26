package com.nirbhay.smartchef.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    val name: String,
    val ingredients: String,
    val instructions: String,
    val cookingTime: String,
    val imageUrl: String = ""
) : Parcelable
