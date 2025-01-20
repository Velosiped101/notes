package com.example.notes.data.remote

import com.google.gson.annotations.SerializedName

data class FoodApiResponse(
    @SerializedName("products") val products: List<Product>?,
)

data class Product(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("nutriments") val nutriments: Nutriments?,
    @SerializedName("image_url") val imageUrl: String?
)

data class Nutriments(
    @SerializedName("fat") val fat: Float?,
    @SerializedName("proteins") val proteins: Float?,
    @SerializedName("carbohydrates") val carbohydrates: Float?
)
