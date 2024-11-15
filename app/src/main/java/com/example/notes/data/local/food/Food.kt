package com.example.notes.data.local.food

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var foodName: String,
    var protein: Int,
    var fat: Int,
    var carbs: Int
)