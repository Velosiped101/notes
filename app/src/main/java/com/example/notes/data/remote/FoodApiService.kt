package com.example.notes.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {
    @GET(FoodApiConstants.BASE_URL_ENDPOINT)
    suspend fun getFood(
        @Query("search_terms") searchTerms: String,
        @Query("fields") fields: String = "product_name,nutriments",
        @Query("sort_by") sortBy: String = "popularity_key",
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1
    ): FoodApiResponse
}