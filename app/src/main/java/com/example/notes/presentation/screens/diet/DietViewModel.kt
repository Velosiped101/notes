package com.example.notes.presentation.screens.diet

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.DietRepository
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.data.remote.FoodApiService
import com.example.notes.utils.FoodHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.security.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.concurrent.timer

class DietViewModel(
    private val repository: DietRepository = Service.dietRepository
): ViewModel() {
    private val foodItemState = FoodItemState()
    val foodHolderState: MutableState<FoodHolder<List<Food>>> = mutableStateOf(FoodHolder.Start())
    val foodListState = repository.getAll()
    val isInDeleteMode = mutableStateOf(false)
    val elementsToDelete = mutableStateListOf<Food>()
    val searchText = mutableStateOf("")
    val massText = mutableStateOf("")
    val pickedFood = mutableStateOf(Food())
    val pickedFoodList = mutableStateMapOf<Food, Int>()
    val mealHistoryList = repository.getMealHistory()

    fun getFoodById(id : Int?): FoodItemState {
        if (id == null) {
            foodItemState.nameState.value = ""
            foodItemState.proteinState.value = ""
            foodItemState.fatState.value = ""
            foodItemState.carbsState.value = ""
        } else {
            viewModelScope.launch {
                try {
                    val foodById = repository.getFoodById(id)
                    foodItemState.nameState.value = foodById.foodName
                    foodItemState.proteinState.value = foodById.protein.toString()
                    foodItemState.fatState.value = foodById.fat.toString()
                    foodItemState.carbsState.value = foodById.carbs.toString()
                }
                catch (e: Exception) {
                    Log.e("tag", e.message.toString())
                }
            }
        }
        return foodItemState
    }

    fun onSelectedForDelete(food: Food): MutableList<Food> {
        if (elementsToDelete.contains(food)) elementsToDelete.remove(food)
        else elementsToDelete.add(food)
        return elementsToDelete
    }

    fun insertFood(id: Int?){
        viewModelScope.launch {
            repository.insert(
                Food(
                    id = id,
                    foodName = foodItemState.nameState.value,
                    protein = foodItemState.proteinState.value.toInt(),
                    fat = foodItemState.fatState.value.toInt(),
                    carbs = foodItemState.carbsState.value.toInt()
                )
            )
        }
    }

    fun deleteFood() {
        viewModelScope.launch {
            repository.delete(elementsToDelete)
        }
        isInDeleteMode.value = false
    }

    fun onLongPress() {
        isInDeleteMode.value = !isInDeleteMode.value
    }

    fun onSearch(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            foodHolderState.value = repository.getCombinedFoodList(text)
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

data class FoodItemState(
    var nameState: MutableState<String> = mutableStateOf(""),
    var proteinState: MutableState<String> = mutableStateOf(""),
    var fatState: MutableState<String> = mutableStateOf(""),
    var carbsState: MutableState<String> = mutableStateOf(""),
)