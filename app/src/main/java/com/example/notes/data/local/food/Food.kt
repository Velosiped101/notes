package com.example.notes.data.local.food

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var name: String,
    var protein: Double,
    var fat: Double,
    var carbs: Double,
    var imageUrl: String?
)