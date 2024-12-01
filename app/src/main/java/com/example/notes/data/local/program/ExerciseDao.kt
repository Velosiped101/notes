package com.example.notes.data.local.program

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM Exercise")
    fun getAll(): Flow<List<Exercise>>
}