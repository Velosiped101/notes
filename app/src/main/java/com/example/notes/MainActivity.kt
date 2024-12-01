package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.notes.presentation.screens.diet.DietViewModel
import com.example.notes.presentation.navigation.Navigation
import com.example.notes.presentation.screens.training.ProgramViewModel
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val dietViewModel = viewModel(modelClass = DietViewModel::class.java)
            val programViewModel = viewModel(modelClass = ProgramViewModel::class.java)
            NotesTheme {
                val programList = programViewModel.programList.collectAsState(initial = emptyList()).value
                val todayProgram = programList.filter { it.dayOfWeek == programViewModel.currentDayOfWeek }
                Navigation(
                    navController = navController,
                    foodItemState = { dietViewModel.getFoodById(it) },
                    foodList = dietViewModel.foodListState.collectAsState(emptyList()).value,
                    onInsert = { dietViewModel.insertFood(null) },
                    onUpdate = { dietViewModel.insertFood(it) },
                    onLongClick = { dietViewModel.onLongPress() },
                    isInDeleteMode = dietViewModel.isInDeleteMode,
                    onSelectedForDelete = { dietViewModel.onSelectedForDelete(it) },
                    selectedItems = dietViewModel.elementsToDelete.toMutableList(),
                    onDelete = { dietViewModel.deleteFood() },
                    searchText = dietViewModel.searchText,
                    onSearch = { dietViewModel.onSearch(it) },
                    foodHolderState = dietViewModel.foodHolderState,
                    massText = dietViewModel.massText,
                    pickedFood = dietViewModel.pickedFood,
                    onDialogConfirm = {dietViewModel.addFoodToPickedList()},
                    pickedFoodList = dietViewModel.pickedFoodList,
                    onConfirm = { dietViewModel.addToHistory() },
                    mealHistory = dietViewModel.mealHistoryList.collectAsState(initial = emptyList()).value,
                    exerciseList = programViewModel.exerciseList.collectAsState(initial = emptyList()).value,
                    insertToProgram = { programViewModel.insertToProgram(it) },
                    deleteFromProgram = { programViewModel.deleteFromProgram(it) },
                    programList = programList,
                    todayProgram = todayProgram,
                    date = programViewModel.date,
                    dayType = if (todayProgram.isEmpty()) "Rest day" else "Training day"
                )
            }
        }
    }
}