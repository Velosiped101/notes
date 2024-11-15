package com.example.notes.data

import android.content.Context
import com.example.notes.data.local.program.Exercise
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(
    context: Context
): NotesRepository<Exercise> {
    override fun getAll(): Flow<List<Exercise>> {
        TODO("Not yet implemented")
    }

    override fun getSearched(text: String): List<Exercise> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(list: List<Exercise>) {
        TODO("Not yet implemented")
    }

    override suspend fun update(element: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(element: Exercise) {
        TODO("Not yet implemented")
    }
}