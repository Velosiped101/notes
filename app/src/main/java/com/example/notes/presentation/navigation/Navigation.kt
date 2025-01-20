package com.example.notes.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil3.Bitmap
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.Program
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.presentation.screens.MainScreen
import com.example.notes.presentation.screens.diet.AddMealScreen
import com.example.notes.presentation.screens.diet.FoodManagerScreen
import com.example.notes.presentation.screens.diet.NewRecipeScreen
import com.example.notes.presentation.screens.training.ProgramEditScreen
import com.example.notes.presentation.screens.training.ProgramExecScreen
import com.example.notes.utils.FoodHolder

@Composable
fun Navigation(
    navController: NavHostController,
    foodList: List<Food>,
    deleteFromFoodDb: (SnapshotStateList<Food>) -> Unit,
    insertToFoodDb: (Food) -> Unit,
    onSearch: () -> Unit,
    foodHolderState: MutableState<FoodHolder<List<Food>>>,
    onDialogConfirm: () -> Unit,
    massText: MutableState<String>,
    pickedFood: MutableState<Food>,
    pickedFoodList: MutableMap<Food, Int>,
    onConfirm: () -> Unit,
    mealHistory: List<MealHistory>,
    exerciseList: List<Exercise>,
    programList: List<Program>,
    insertToProgram: (Program) -> Unit,
    deleteFromProgram: (Program) -> Unit,
    date: String,
    dayType: String,
    todayProgram: List<Program>,
    searchText: MutableState<String>,
    getFromLocal: MutableState<Boolean>,
    getFromRemote: MutableState<Boolean>,
    onCreateFromRecipe: (Food) -> Unit
) {
    NavHost(navController = navController, startDestination = Routes.Main.name) {
        composable(Routes.Main.name) {
            MainScreen(
                mealHistory = mealHistory,
                onCreateNewRecipe = {
                    navController.navigate(Routes.NewRecipe.name)
                },
                onAddMeal = {
                    navController.navigate(Routes.AddMeal.name)
                },
                onManageLocalFoodDb = {
                    navController.navigate(Routes.FoodManager.name)
                },
                onProgramFieldClicked = {
                    navController.navigate(
                        Routes.ProgramEdit.name
                    )
                },
                date = date,
                dayType = dayType,
                startProgram = {
                    navController.navigate(Routes.ProgramExec.name)
                }
            )
        }
        composable(Routes.AddMeal.name) { AddMealScreen(
            onSearch = onSearch,
            foodHolderState = foodHolderState,
            massText = massText,
            onClick = { onDialogConfirm() },
            pickedFoodList = pickedFoodList,
            pickedFood = pickedFood,
            onBackButtonClicked = { navController.navigateUp() },
            onConfirm = {
                onConfirm()
                navController.navigate(Routes.Main.name) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            searchText = searchText,
            getFromLocal = getFromLocal,
            getFromRemote = getFromRemote
        ) }
        composable(Routes.FoodManager.name) {
            FoodManagerScreen(
                onBackButtonClicked = { navController.navigateUp() },
                foodList = foodList,
                onDelete = { deleteFromFoodDb(it) },
                onConfirm = { insertToFoodDb(it) }
            )
        }
        composable(Routes.ProgramEdit.name) {
            ProgramEditScreen(
                onBackClick = {navController.navigateUp()},
                exerciseList = exerciseList,
                onClick = insertToProgram,
                onDelete = deleteFromProgram,
                programList = programList
            )
        }
        composable(Routes.ProgramExec.name) {
            ProgramExecScreen(programList = todayProgram)
        }
        composable(Routes.NewRecipe.name) {
            NewRecipeScreen(
                onBackButtonClicked = { navController.navigateUp() }
            ) {
                onCreateFromRecipe(it)
            }
        }
    }
}

enum class Routes {
    Main,
    FoodManager,
    AddMeal,
    ProgramEdit,
    ProgramExec,
    NewRecipe
}