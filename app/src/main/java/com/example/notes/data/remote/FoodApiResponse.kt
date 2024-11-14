package com.example.notes.data.remote

import com.google.gson.annotations.SerializedName

data class FoodApiResponse(
    @SerializedName("products") val products: List<Product>?,       // Список продуктов
)

data class Product(
    @SerializedName("product_name") val productName: String?,      // Название продукта
    @SerializedName("nutriments") val nutriments: Nutriments?,     // Пищевая ценность продукта
)

data class Nutriments(
    @SerializedName("fat") val fat: Float?,                        // Жиры (граммы)
    @SerializedName("proteins") val proteins: Float?,              // Белки (граммы)
    @SerializedName("carbohydrates") val carbohydrates: Float?     // Углеводы (граммы)
)
