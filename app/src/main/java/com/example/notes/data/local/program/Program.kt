package com.example.notes.data.local.program

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise"],
            onDelete = ForeignKey.CASCADE
            )
    ]
)
data class Program(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val dayOfWeek: Int,
    val exercise: String,
    val sets: Int,
    val reps: Int
)