package com.example.notes.presentation.screens.diet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.DietRepository
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.utils.FoodHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class DietViewModel(
    private val repository: DietRepository = Service.dietRepository
): ViewModel() {
    val foodHolderState: MutableState<FoodHolder<List<Food>>> = mutableStateOf(FoodHolder.Start())
    val foodListState = repository.getAll()
    val massText = mutableStateOf("")
    val pickedFood = mutableStateOf(Food(
        foodName = "",
        protein = 0,
        fat = 0,
        carbs = 0
    ))
    val pickedFoodList = mutableStateMapOf<Food, Int>()
    val mealHistoryList = repository.getMealHistory()
    val searchText = mutableStateOf("")
    val getFromLocal = mutableStateOf(false)
    val getFromRemote = mutableStateOf(true)

    fun deleteFood(list: SnapshotStateList<Food>) {
        viewModelScope.launch {
            repository.delete(list)
        }
    }

    fun createFood(food: Food){
        viewModelScope.launch {
            repository.insert(
                food
            )
        }
    }

    fun onSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            foodHolderState.value = repository.getCombinedFoodList(
                text = searchText.value,
                getFromLocal = getFromLocal.value,
                getFromRemote = getFromRemote.value
            )
        }
    }

    fun addFoodToPickedList () {
        pickedFoodList[pickedFood.value] = massText.value.toInt()
    }

    fun addToHistory() {
        val time = Calendar.getInstance()
        viewModelScope.launch {
            pickedFoodList.entries.forEach() {
                repository.insertToHistory(
                    MealHistory(
                        time = "${time.get(Calendar.HOUR_OF_DAY)}:${time.get(Calendar.MINUTE)}",
                        name = it.key.foodName,
                        protein = it.key.protein,
                        fat = it.key.fat,
                        carbs = it.key.carbs,
                        mass = it.value,
                        totalCal = ((it.key.carbs+it.key.protein)*4 + it.key.fat*9)*it.value/100
                    )
                )
            }
        }
    }
}