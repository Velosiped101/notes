package com.example.notes.data

import android.content.Context
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.Program
import kotlinx.coroutines.flow.Flow

class ProgramRepository(
    context: Context
): NotesRepository<Program> {
    override fun getAll(): Flow<List<Program>> {
        TODO("Not yet implemented")
    }

    override fun getSearched(text: String): List<Program> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(list: List<Program>) {
        TODO("Not yet implemented")
    }

    override suspend fun update(element: Program) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(element: Program) {
        TODO("Not yet implemented")
    }
}