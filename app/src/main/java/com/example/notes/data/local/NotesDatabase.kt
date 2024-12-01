package com.example.notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.R
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.ExerciseDao
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.food.FoodDao
import com.example.notes.data.local.program.Program
import com.example.notes.data.local.program.ProgramDao
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.data.local.saveddata.mealhistory.MealHistoryDao

@Database(
    entities = [Food::class, Exercise::class, MealHistory::class, Program::class],
    version = 2
)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun mealHistoryDao(): MealHistoryDao
    abstract fun programDao(): ProgramDao

    companion object {
        fun createDb(context: Context): NotesDatabase {
            return Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "notes-database"
            )
                .createFromAsset("notesAssetDatabase.db")
                .build()
        }
    }
}