package com.example.notes

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.notes.Service.dietRepository
import java.util.concurrent.TimeUnit

class NotesApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        Service.provide(this)

        class CleanHistoryWorker(
            appContext: Context,
            workerParameters: WorkerParameters
        ): CoroutineWorker(appContext,workerParameters) {
            override suspend fun doWork(): Result {
                dietRepository.clearMealHistory()
                return Result.success()
            }
        }
        val clearMealHistoryRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<CleanHistoryWorker>()
                .setInitialDelay(
                    dietRepository.getWorkDelay(),
                    TimeUnit.SECONDS
                )
                .build()
        WorkManager
            .getInstance(this)
            .enqueueUniqueWork(
                "clearHistory",
                ExistingWorkPolicy.KEEP,
                clearMealHistoryRequest
            )
    }
}