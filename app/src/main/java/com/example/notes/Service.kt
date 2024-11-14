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
    lateinit var dietRepository: DietRepository

    fun provide(context: Context){
        dietRepository = DietRepository(context)
    }
}