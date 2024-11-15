package com.example.notes.presentation.screens.training

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.notes.Service
import com.example.notes.data.ExerciseRepository
import com.example.notes.data.ProgramRepository
import com.example.notes.data.local.program.Program
import com.example.notes.utils.DayHolder
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.DayState

class ProgramViewModel(
    private val programRepository: ProgramRepository = Service.programRepository,
    private val exerciseRepository: ExerciseRepository = Service.exerciseRepository
): ViewModel() {
    val dayHolderState: MutableState<DayHolder<DayState>> = mutableStateOf(
            DayHolder.Monday(mondayState = DayState(
                pickedDay = DayOfWeek.Monday,
                program = emptyList()
            ))
        )
}