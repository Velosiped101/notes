package com.example.notes.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.presentation.screens.maincards.DietCard
import com.example.notes.presentation.screens.maincards.ExercisesCard

@Composable
fun MainScreen(
    onDietFieldClicked: () -> Unit,
    mealHistory: List<MealHistory>
) {
    Column {
        DietCard(onDietFieldClicked = onDietFieldClicked, mealHistory = mealHistory)
        ExercisesCard()
    }
}

@Preview (showSystemUi = true)
@Composable
private fun PreviewMainScreen() {
    MainScreen({}, emptyList())
}