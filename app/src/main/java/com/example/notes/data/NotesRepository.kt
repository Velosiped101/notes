package com.example.notes.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository<T> {
    fun getAll(): Flow<List<T>>
    fun getSearched(text: String): List<T>
    suspend fun insert(element: T)
    suspend fun update(element: T)
    suspend fun delete(list: List<T>)
}