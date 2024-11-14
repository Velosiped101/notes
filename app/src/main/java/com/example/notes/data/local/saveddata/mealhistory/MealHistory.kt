package com.example.notes.data.local.saveddata.mealhistory

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notes.data.local.food.Food
import java.util.Date

@Entity
data class MealHistory(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val time: String,
    val name: String,
    val protein: Int,
    val fat: Int,
    val carbs: Int,
    val totalCal: Int,
    val mass: Int
)