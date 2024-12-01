package com.example.notes.presentation.screens.training

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.ExerciseRepository
import com.example.notes.data.ProgramRepository
import com.example.notes.data.local.program.Program
import com.example.notes.utils.DayOfWeek
import kotlinx.coroutines.launch
import java.util.Calendar

class ProgramViewModel(
    private val programRepository: ProgramRepository = Service.programRepository,
    private val exerciseRepository: ExerciseRepository = Service.exerciseRepository
): ViewModel() {
    val exerciseList = exerciseRepository.getAll()
    val dayOfWeek = mutableStateOf(DayOfWeek.Monday)
    val programList = programRepository.getAll()
    private val calendar = Calendar.getInstance()
    val date = "${calendar.get(Calendar.DAY_OF_MONTH)}." +
            "${calendar.get(Calendar.MONTH)+1}." +
            "${calendar.get(Calendar.YEAR)}"
    private val weekMap = mapOf(
        2 to DayOfWeek.Monday,
        3 to DayOfWeek.Tuesday,
        4 to DayOfWeek.Wednesday,
        5 to DayOfWeek.Thursday,
        6 to DayOfWeek.Friday,
        7 to DayOfWeek.Saturday,
        1 to DayOfWeek.Sunday
    )
    val currentDayOfWeek: String = weekMap[calendar.get(Calendar.DAY_OF_WEEK)]!!.name

    fun insertToProgram(item: Program) {
        viewModelScope.launch {
            programRepository.insertToProgram(item)
        }
    }

    fun deleteFromProgram(item: Program) {
        viewModelScope.launch {
            programRepository.deleteFromProgram(item)
        }
    }
}