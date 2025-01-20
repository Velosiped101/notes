package com.example.notes.data.local.food

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * from Food")
    fun getFoodList(): Flow<List<Food>>

    @Query("SELECT * from Food WHERE name LIKE '%' || :searchedFood || '%'")
    fun getSearchedFoodList(searchedFood: String): List<Food>

    @Query("SELECT * from Food WHERE id = :index")
    suspend fun getFoodById(index: Int): Food

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(listOfIds: List<Food>)
}