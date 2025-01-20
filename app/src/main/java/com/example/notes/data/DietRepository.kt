package com.example.notes.data

import android.content.ContentProvider
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import coil3.Bitmap
import coil3.Uri
import coil3.util.CoilUtils
import com.example.notes.NotesApplication
import com.example.notes.Service
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.food.FoodDao
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.data.local.saveddata.mealhistory.MealHistoryDao
import com.example.notes.data.remote.FoodApiConstants
import com.example.notes.data.remote.FoodApiResponse
import com.example.notes.data.remote.FoodApiService
import com.example.notes.data.remote.Product
import com.example.notes.utils.FoodHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Calendar
import kotlin.io.path.Path

class DietRepository(
    private val dao: FoodDao = Service.db.foodDao(),
    private val mealHistoryDao: MealHistoryDao = Service.db.mealHistoryDao()
): NotesRepository<Food> {
    private val apiService: FoodApiService = Retrofit.Builder()
        .baseUrl(FoodApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FoodApiService::class.java)

    override fun getAll(): Flow<List<Food>> {
        return dao.getFoodList()
    }

    override fun getSearched(text: String): List<Food> {
        return dao.getSearchedFoodList(text)
    }

    override suspend fun delete(list: List<Food>) {
        dao.deleteFood(list)
    }

    override suspend fun update(element: Food) {
        dao.updateFood(element)
    }

    override suspend fun insert(element: Food) {
        dao.insertFood(element)
    }

    suspend fun insertToHistory(element: MealHistory) {
        mealHistoryDao.insert(element)
    }

    fun getMealHistory(): Flow<List<MealHistory>> {
        return mealHistoryDao.getAll()
    }

    private suspend fun getFoodFromApi(text:String): FoodApiResponse {
        return apiService.getFood(text)
    }

    suspend fun getCombinedFoodList(
        text: String,
        getFromLocal: Boolean,
        getFromRemote: Boolean,
    ): FoodHolder<List<Food>> {
        return try {
            if (getFromLocal && getFromRemote) {
                val localList = getSearched(text)
                val remoteList = getFoodFromApi(text).products.let {
                    formatRemoteList(it)
                }
                val data = mutableListOf<Food>().plus(localList).plus(remoteList)
                FoodHolder.Success(data)
            }
            else if (getFromLocal) {
                val localList = getSearched(text)
                FoodHolder.Success(localList)
            }
            else if (getFromRemote) {
                val remoteList = getFoodFromApi(text).products.let {
                    formatRemoteList(it)
                }
                FoodHolder.Success(remoteList)
            }
            else {
                FoodHolder.Success(emptyList())
            }
        } catch(e: Exception) {
            FoodHolder.Error(e)
        }
    }

    private fun formatRemoteList(list: List<Product>?): List<Food> {
        return list?.mapNotNull {
            if (
                it.productName == null || it.productName == "" ||
                it.nutriments == null ||
                it.nutriments.proteins == null ||
                it.nutriments.fat == null ||
                it.nutriments.carbohydrates == null
            ) null
            else Food(
                name = it.productName,
                protein = it.nutriments.proteins.toDouble(),
                fat = it.nutriments.fat.toDouble(),
                carbs = it.nutriments.carbohydrates.toDouble(),
                imageUrl = it.imageUrl
            )
        } ?: emptyList()
    }

    suspend fun clearMealHistory() {
        mealHistoryDao.clear()
    }

    fun getWorkDelay(): Long {
        val dayDurationInSec = 24*3600
        val dateInstance = Calendar.getInstance()
        val startTime = dateInstance.get(Calendar.HOUR_OF_DAY) * 3600 +
                    dateInstance.get(Calendar.MINUTE) * 60 +
                    dateInstance.get(Calendar.SECOND)
        return (dayDurationInSec - startTime).toLong()
    }
}