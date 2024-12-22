package com.example.notes.data

import com.example.notes.Service
import com.example.notes.data.local.program.Program
import com.example.notes.data.local.program.ProgramDao
import kotlinx.coroutines.flow.Flow

class ProgramRepository(
    private val dao: ProgramDao = Service.db.programDao()
) {
    fun getAll(): Flow<List<Program>> {
        return dao.getAll()
    }

    suspend fun insertToProgram(item: Program) {
        dao.insert(item)
    }

    suspend fun deleteFromProgram(item: Program) {
        dao.delete(item)
    }

//    fun getAllProgress(day: Int, month: Int, year: Int): Flow<List<ProgramProgress>> {
//        return progressDao.getAll(day, month, year)
//    }
}