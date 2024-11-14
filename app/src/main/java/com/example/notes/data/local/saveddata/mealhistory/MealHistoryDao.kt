package com.example.notes.data.local.saveddata.mealhistory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealHistoryDao {
    @Query("SELECT * FROM MealHistory")
    fun getAll(): Flow<List<MealHistory>>

    @Insert
    suspend fun insert(item: MealHistory)

    @Query("DELETE FROM MealHistory")
    suspend fun clear()
}