package com.example.notes

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notes.data.DietRepository
import com.example.notes.data.ExerciseRepository
import com.example.notes.data.ProgramRepository
import com.example.notes.data.local.NotesDatabase
import com.example.notes.data.remote.FoodApiConstants
import com.example.notes.data.remote.FoodApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

object Service {
    lateinit var db: NotesDatabase
    lateinit var dietRepository: DietRepository
    lateinit var exerciseRepository: ExerciseRepository
    lateinit var programRepository: ProgramRepository

    fun provide(context: Context){
        db = NotesDatabase.createDb(context)
        dietRepository = DietRepository()
        exerciseRepository = ExerciseRepository()
        programRepository = ProgramRepository()
    }
}