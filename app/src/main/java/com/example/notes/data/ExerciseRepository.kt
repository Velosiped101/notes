package com.example.notes.data

import android.content.Context
import com.example.notes.Service
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.ExerciseDao
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(
    private val dao: ExerciseDao = Service.db.exerciseDao()
): NotesRepository<Exercise> {
    override fun getAll(): Flow<List<Exercise>> {
        return dao.getAll()
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