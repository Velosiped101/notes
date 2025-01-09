package com.example.notes.presentation.screens.diet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.data.local.food.Food
import com.example.notes.presentation.screens.diet.components.FoodCardItem

@Composable
fun FoodManagerScreen(
    foodList: List<Food>,
    onBackButtonClicked: () -> Unit,
    onConfirm: (Food) -> Unit,
    onDelete: (SnapshotStateList<Food>) -> Unit
) {
    val isDialogActive = remember {
        mutableStateOf(false)
    }
    val isInDeleteMode = remember {
        mutableStateOf(false)
    }
    val selectedForDeleteList = remember {
        mutableStateListOf<Food>()
    }
    val pickedFood = remember {
        mutableStateOf<Food?>(null)
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TopBar(
                onAddButtonClicked = {
                    pickedFood.value = null
                    isDialogActive.value = true
                },
                onBackButtonClicked = { onBackButtonClicked() },
                isInDeleteMode = isInDeleteMode.value,
                onDelete = {
                    onDelete(selectedForDeleteList)
                    isInDeleteMode.value = false
                }
            )
            LazyColumn {
                items(foodList) { item ->
                    val colors = if (selectedForDeleteList.contains(item))
                        CardDefaults.cardColors(containerColor = Color.Red)
                    else CardDefaults.cardColors(containerColor = Color.White)
                    FoodCardItem(
                        food = item,
                        onFoodItemClicked = {
                            when (isInDeleteMode.value) {
                                true -> {
                                    when (selectedForDeleteList.contains(item)) {
                                        true -> {
                                            if (selectedForDeleteList.size == 1) {
                                                selectedForDeleteList.remove(item)
                                                isInDeleteMode.value = false
                                            }
                                            else selectedForDeleteList.remove(item)
                                        }
                                        false -> { selectedForDeleteList.add(item) }
                                    }
                                }
                                false -> {
                                    pickedFood.value = item
                                    isDialogActive.value = true
                                }
                            }
                        },
                        onLongClick = {
                            if (!isInDeleteMode.value) {
                                isInDeleteMode.value = true
                                selectedForDeleteList.add(item)
                            }
                        },
                        colors = colors
                    )
                }
            }
        }
    }
    if (isDialogActive.value)
        EditFoodDialog(
            isDialogActive = isDialogActive,
            food = pickedFood.value
        ) {
            if (pickedFood.value != null) {
                onDelete(mutableStateListOf(pickedFood.value!!))
            }
            onConfirm(it)
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditFoodDialog(
    modifier: Modifier = Modifier,
    isDialogActive: MutableState<Boolean>,
    food: Food?,
    onConfirm: (Food) -> Unit
) {
    BasicAlertDialog(onDismissRequest = { isDialogActive.value = false }) {
        Card(
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val foodState = if (food != null)
                    remember {
                        mutableStateOf(food)
                    }
                else
                    remember {
                    mutableStateOf(
                        Food(foodName = "", protein = 0, fat = 0, carbs = 0)
                    )
                }
                val isInvalidInput = remember {
                    mutableStateOf(false)
                }
                InputField(
                    inputText = foodState.value.foodName,
                    fieldName = "Name",
                    onValueChange = {foodState.value.foodName = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isInvalidInput = isInvalidInput
                )
                InputField(
                    inputText = foodState.value.protein.toString(),
                    fieldName = "Protein",
                    onValueChange = {foodState.value.protein = it.toInt()},
                    isInvalidInput = isInvalidInput
                )
                InputField(
                    inputText = foodState.value.fat.toString(),
                    fieldName = "Fat",
                    onValueChange = {foodState.value.fat = it.toInt()},
                    isInvalidInput = isInvalidInput
                )
                InputField(
                    inputText = foodState.value.carbs.toString(),
                    fieldName = "Carbs",
                    onValueChange = {foodState.value.carbs = it.toInt()},
                    isInvalidInput = isInvalidInput
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { isDialogActive.value = false }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        if (!isInvalidInput.value) {
                            onConfirm(foodState.value)
                            isDialogActive.value = false
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.confirm),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InputField(
    inputText: String,
    onValueChange: (String) -> Unit,
    fieldName: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
    ),
    isInvalidInput: MutableState<Boolean>
) {
    val inputState = remember {
        mutableStateOf(inputText)
    }
    val isError = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = fieldName)
        OutlinedTextField(
            value = inputState.value,
            onValueChange = {
                inputState.value = it
                try {
                    onValueChange(it)
                    isError.value = false
                    isInvalidInput.value = false
                }
                catch (e: Exception) {
                    isError.value = true
                    isInvalidInput.value = true
                }
            },
            isError = isError.value,
            keyboardOptions = keyboardOptions,
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onAddButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    isInDeleteMode: Boolean,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Food manager")
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBackButtonClicked() },
                painter = painterResource(id = R.drawable.back),
                contentDescription = null
            )
        },
        actions = {
            if (!isInDeleteMode)
                IconButton(onClick = onAddButtonClicked) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.add_plus),
                        contentDescription = null
                    )
                }
            if (isInDeleteMode)
                IconButton(onClick = onDelete) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = null
                    )
                }
        }
    )
}