package com.example.notes.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.presentation.screens.maincards.DietCard
import com.example.notes.presentation.screens.maincards.ExercisesCard

@Composable
fun MainScreen(
    onCreateNewRecipe: () -> Unit,
    onAddMeal: () -> Unit,
    onManageLocalFoodDb: () -> Unit,
    mealHistory: List<MealHistory>,
    onProgramFieldClicked: () -> Unit,
    date: String,
    dayType: String,
    startProgram: () -> Unit
) {
    Column {
        DietCard(
            onCreateNewRecipe = onCreateNewRecipe,
            onAddMeal = onAddMeal,
            onManageLocalFoodDb = onManageLocalFoodDb,
            mealHistory = mealHistory
        )
        ExercisesCard(onClick = onProgramFieldClicked, date = date, dayType = dayType, startProgram = startProgram)
    }
}