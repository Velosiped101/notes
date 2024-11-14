package com.example.notes.data.local.program

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val type: String
)