package com.example.notes.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.notes.data.local.NotesDatabase
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.data.remote.FoodApiConstants
import com.example.notes.data.remote.FoodApiResponse
import com.example.notes.data.remote.FoodApiService
import com.example.notes.utils.FoodHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class DietRepository(
    context: Context
): NotesRepository<Food> {
    private val db = NotesDatabase.createDb(context)
    private val dao = db.foodDao()
    private val mealHistoryDao = db.mealHistoryDao()
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

    suspend fun getFoodById(id: Int): Food {
        return dao.getFoodById(id)
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

    suspend fun getCombinedFoodList(text: String): FoodHolder<List<Food>> {
        return try {
            val localList = getSearched(text)
            val remoteList = getFoodFromApi(text).products?.mapNotNull {
                if (
                    it.productName == null || it.productName == "" ||
                    it.nutriments == null ||
                    it.nutriments.proteins == null ||
                    it.nutriments.fat == null ||
                    it.nutriments.carbohydrates == null
                ) null
                else Food(
                    foodName = it.productName,
                    protein = it.nutriments.proteins.toInt(),
                    fat = it.nutriments.fat.toInt(),
                    carbs = it.nutriments.carbohydrates.toInt()
                )
            } ?: throw Throwable("error")
            val data = mutableListOf<Food>().plus(localList).plus(remoteList)
            FoodHolder.Success(data)
        } catch(e: Exception) {
            FoodHolder.Error(e)
        }
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