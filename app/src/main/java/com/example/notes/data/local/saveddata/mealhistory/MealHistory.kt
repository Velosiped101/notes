package com.example.notes.data.local.saveddata.mealhistory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MealHistory(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val time: String,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val totalCal: Double,
    val mass: Int
)