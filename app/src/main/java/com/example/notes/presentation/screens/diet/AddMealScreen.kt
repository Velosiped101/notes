package com.example.notes.presentation.screens.diet

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.data.local.food.Food
import com.example.notes.presentation.screens.diet.components.FoodCardItem
import com.example.notes.utils.FoodHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    text: MutableState<String>,
    onSearch: (String) -> Unit,
    foodHolderState: MutableState<FoodHolder<List<Food>>>,
    massText: MutableState<String>,
    pickedFood: MutableState<Food>,
    onClick: () -> Unit,
    pickedFoodList: MutableMap<Food, Int>,
    onBackButtonClicked: () -> Unit,
    onConfirm: () -> Unit
) {
    BackHandler {
        text.value = ""
        pickedFoodList.clear()
        foodHolderState.value = FoodHolder.Start()
        onBackButtonClicked()
    }
    val isDialogActive = remember {
        mutableStateOf(false)
    }
    val searchJob = remember {
        mutableStateOf<Job?>(null)
    }
    val scope = rememberCoroutineScope()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    SearchTextField(text = text) {
                        if (it.isBlank()) {
                            searchJob.value?.cancel()
                            searchJob.value = null
                            foodHolderState.value = FoodHolder.Start()
                        }
                        else {
                            searchJob.value?.cancel()
                            foodHolderState.value = FoodHolder.TextChange()
                            searchJob.value = scope.launch {
                                delay(1000L)
                                foodHolderState.value = FoodHolder.Loading()
                                onSearch(it)
                            }
                        }
                    }
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                pickedFoodList.clear()
                                text.value = ""
                                onBackButtonClicked()
                            },
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = null
                    )
                },
                actions = {
                    AnimatedVisibility(
                        visible = pickedFoodList.values.isNotEmpty(),
                        enter = expandHorizontally(expandFrom = Alignment.End),
                        exit = shrinkHorizontally(shrinkTowards = Alignment.End)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    onConfirm()
                                    pickedFoodList.clear()
                                },
                            painter = painterResource(id = R.drawable.baseline_done_24),
                            contentDescription = null
                        )
                    }
                }
            )
            when (foodHolderState.value) {
                is FoodHolder.Start ->
                    if (pickedFoodList.isEmpty()) StartScreen()
                    else PickedFoodListScreen(
                        pickedFoodList = pickedFoodList,
                        massText = massText,
                        isDialogActive = isDialogActive,
                        text = text,
                        onClick = onClick,
                        searchJob = searchJob,
                        foodHolderState = foodHolderState
                    )
                is FoodHolder.TextChange -> {}
                is FoodHolder.Loading -> LoadingScreen()
                is FoodHolder.Success -> SuccessScreen(
                    foodList = foodHolderState.value.data ?: emptyList(),
                    isDialogActive = isDialogActive,
                    pickedFood = pickedFood,
                    massText = massText,
                    text = text,
                    onClick = onClick,
                    searchJob = searchJob,
                    foodHolderState = foodHolderState
                )
                is FoodHolder.Error -> ErrorScreen()
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(80.dp)
                .alpha(.2f),
            color = Color.Gray,
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun SuccessScreen(
    foodList: List<Food>,
    isDialogActive: MutableState<Boolean>,
    pickedFood: MutableState<Food>,
    massText: MutableState<String>,
    text: MutableState<String>,
    onClick: () -> Unit,
    searchJob: MutableState<Job?>,
    foodHolderState: MutableState<FoodHolder<List<Food>>>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(foodList) { item ->
            FoodCardItem(
                food = item,
                onFoodItemClicked = {
                    isDialogActive.value = true
                    pickedFood.value = item
                },
                onLongClick = {  },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            )
            if (isDialogActive.value) {
                MassDialog(
                    isDialogActive,
                    massText,
                    text,
                    onClick,
                    searchJob,
                    foodHolderState
                )
            }
        }
    }
}

@Composable
private fun ErrorScreen(modifier: Modifier = Modifier) {
    Text(text = "error")
}

@Composable
private fun PickedFoodListScreen(
    pickedFoodList: MutableMap<Food, Int>,
    massText: MutableState<String>,
    isDialogActive: MutableState<Boolean>,
    text: MutableState<String>,
    onClick: () -> Unit,
    searchJob: MutableState<Job?>,
    foodHolderState: MutableState<FoodHolder<List<Food>>>
) {
    LazyColumn {
        items(pickedFoodList.entries.toList()) {
            PickedFoodListItem(
                name = it.key.foodName,
                mass = it.value,
                onEdit = {
                    massText.value = it.value.toString()
                    isDialogActive.value = true
                }
            ) {
                pickedFoodList.entries.remove(it)
            }
            if (isDialogActive.value) {
                MassDialog(
                    isDialogActive,
                    massText,
                    text,
                    onClick,
                    searchJob,
                    foodHolderState
                )
            }
        }
    }
}

@Composable
private fun StartScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Use a search above to add your meal",
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MassDialog(
    isDialogActive: MutableState<Boolean>,
    massText: MutableState<String>,
    text: MutableState<String>,
    onClick: () -> Unit,
    searchJob: MutableState<Job?>,
    foodHolderState: MutableState<FoodHolder<List<Food>>>
) {
    BasicAlertDialog(
        onDismissRequest = {
            isDialogActive.value = false
            massText.value = ""
            foodHolderState.value = FoodHolder.Start()
        }
    ) {
        val focusRequester = remember {
            FocusRequester()
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Card(
            Modifier.background(Color.Transparent)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Enter mass in grams")
                BasicTextField(
                    value = TextFieldValue(
                        text = massText.value,
                        selection = TextRange(massText.value.length)
                    ),
                    onValueChange = { massText.value = it.text },
                    singleLine = true,
                    modifier = Modifier
                        .height(50.dp)
                        .wrapContentSize()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        showKeyboardOnFocus = true
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onClick()
                            massText.value = ""
                            text.value = ""
                            isDialogActive.value = false
                            searchJob.value = null
                            foodHolderState.value = FoodHolder.Start()
                        }
                    )
                )
                Button(
                    onClick = {
                        onClick()
                        massText.value = ""
                        text.value = ""
                        isDialogActive.value = false
                        searchJob.value = null
                        foodHolderState.value = FoodHolder.Start()
                    },
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    text: MutableState<String>,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onValueChange(text.value)
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 24.sp
        )
    )
}

@Composable
private fun PickedFoodListItem(
    name: String,
    mass: Int,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "$mass g", textAlign = TextAlign.End)
            Icon(
                painter = painterResource(id = R.drawable.outline_edit_24),
                contentDescription = null,
                Modifier
                    .clickable { onEdit() }
                    .size(25.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.rounded_remove_24),
                contentDescription = null,
                Modifier
                    .clickable { onRemove() }
                    .size(25.dp)
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    PickedFoodListItem(name = "Chebupelyahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", mass = 500, onEdit = {}) {}
}