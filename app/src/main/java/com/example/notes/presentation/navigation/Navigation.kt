package com.example.notes.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.collection.MutableIntList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.presentation.screens.MainScreen
import com.example.notes.presentation.screens.diet.AddMealScreen
import com.example.notes.presentation.screens.diet.DietInfoScreen
import com.example.notes.presentation.screens.diet.EditFoodScreen
import com.example.notes.presentation.screens.diet.FoodItemState
import com.example.notes.presentation.screens.diet.FoodManagerScreen
import com.example.notes.presentation.screens.training.ProgramEditScreen
import com.example.notes.utils.DayHolder
import com.example.notes.utils.DayState
import com.example.notes.utils.FoodHolder

@Composable
fun Navigation(
    navController: NavHostController,
    foodItemState: (Int?) -> FoodItemState,
    foodList: List<Food>,
    onUpdate: (Int) -> Unit,
    onInsert: () -> Unit,
    onLongClick: () -> Unit,
    isInDeleteMode: MutableState<Boolean>,
    onSelectedForDelete: (Food) -> Unit,
    selectedItems: MutableList<Food>,
    onDelete: () -> Unit,
    searchText: MutableState<String>,
    onSearch: (String) -> Unit,
    foodHolderState: MutableState<FoodHolder<List<Food>>>,
    onDialogConfirm: () -> Unit,
    massText: MutableState<String>,
    pickedFood: MutableState<Food>,
    pickedFoodList: MutableMap<Food, Int>,
    onConfirm: () -> Unit,
    mealHistory: List<MealHistory>,
    dayHolderState: MutableState<DayHolder<DayState>>
) {
    NavHost(navController = navController, startDestination = Routes.Main.name) {
        composable(Routes.Main.name) {
            MainScreen(
                mealHistory = mealHistory,
                onDietFieldClicked = {
                    navController.navigate(
                        Routes.DietScr.name
                    )
                },
                onProgramFieldClicked = {
                    navController.navigate(
                        Routes.ProgramEdit.name
                    )
                }
            )
        }
        composable(Routes.DietScr.name) {
            DietInfoScreen(
                onAddMealClicked = { navController.navigate(Routes.AddMeal.name) },
                onManageFoodClicked = { navController.navigate(Routes.FoodManager.name) }) {
                navController.navigateUp()
            }
        }
        composable(Routes.AddMeal.name) { AddMealScreen(
            text = searchText,
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
            }
        ) }
        composable(
            route = "${ Routes.EditFood.name }/{id}",
            arguments = listOf(navArgument("id") {type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            EditFoodScreen(
                onBackButtonClicked = {
                    navController.navigateUp()
                },
                onSaveButtonClicked = {
                    id?.let {
                        onUpdate(it)
                        navController.navigateUp()
                    }
                },
                foodItemState = foodItemState(id)
            )
        }
        composable(
            route = Routes.EditFood.name
        ) {
            EditFoodScreen(
                onBackButtonClicked = {
                    navController.navigateUp()
                },
                onSaveButtonClicked = {
                    onInsert()
                    navController.navigateUp()
                },
                foodItemState = foodItemState(null)
            )
        }
        composable(Routes.FoodManager.name) {
            FoodManagerScreen(
                onBackButtonClicked = { navController.navigateUp() },
                onAddButtonClicked = { navController.navigate(Routes.EditFood.name) },
                onFoodItemClicked = { food -> navController.navigate("${Routes.EditFood.name}/${food.id}") },
                foodList = foodList,
                onLongClick = onLongClick,
                isInDeleteMode = isInDeleteMode,
                onSelectedForDelete = onSelectedForDelete,
                selectedItems = selectedItems,
                onDelete = onDelete
            )
        }
        composable(Routes.ProgramEdit.name) {
            ProgramEditScreen(
                dayHolderState = dayHolderState,
                onClick = {navController.navigateUp()}
            )
        }
    }
}

enum class Routes {
    Main,
    DietScr,
    FoodManager,
    AddMeal,
    EditFood,
    ProgramEdit
}