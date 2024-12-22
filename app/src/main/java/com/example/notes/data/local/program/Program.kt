package com.example.notes.data.local.program

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Program(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val dayOfWeek: String,
    val exercise: String,
    val reps: Int
)