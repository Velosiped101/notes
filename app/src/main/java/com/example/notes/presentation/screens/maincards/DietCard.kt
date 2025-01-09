package com.example.notes.presentation.screens.maincards

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.ui.theme.Typography

@Composable
fun DietCard(
    onCreateNewRecipe: () -> Unit,
    onAddMeal: () -> Unit,
    onManageLocalFoodDb: () -> Unit,
    mealHistory: List<MealHistory>
) {
    val isMealHistoryExpanded = remember {
        mutableStateOf(false)
    }
    val isDialogActive = remember {
        mutableStateOf(false)
    }
    val rotationAngle = animateFloatAsState(
        targetValue = if (isMealHistoryExpanded.value) 180f else 0f,
        label = "animate"
    )
    val totalProtein = mealHistory.sumOf { it.protein * it.mass/100 }
    val totalFat = mealHistory.sumOf { it.fat * it.mass/100 }
    val totalCarbs = mealHistory.sumOf { it.carbs * it.mass/100 }
    val totalCals = (totalProtein + totalCarbs) * 4 + totalFat * 9
    val mealHistoryColumnHeight = 180.dp
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { isDialogActive.value = true }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current intake",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$totalCals/2500",
                style = Typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = {totalCals/2500f},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientText(
                    nutrientType = "Protein",
                    nutrientCount = totalProtein,
                    modifier = Modifier.weight(1f)
                )
                NutrientText(
                    nutrientType = "Fat",
                    nutrientCount = totalFat,
                    modifier = Modifier.weight(1f)
                )
                NutrientText(
                    nutrientType = "Carbs",
                    nutrientCount = totalCarbs,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(
                visible = isMealHistoryExpanded.value,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                if (mealHistory.isEmpty())  Text(
                    text = "Empty",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mealHistoryColumnHeight)
                        .wrapContentSize(align = Alignment.Center),
                    textAlign = TextAlign.Center
                )
                else
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(mealHistoryColumnHeight)
                    ) {

                        items(mealHistory) {
                            MealHistoryItem(
                                time = it.time,
                                name = it.name,
                                mass = it.mass,
                                cals = it.totalCal
                            )
                        }
                    }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                painter = painterResource(
                    id = R.drawable.expand_meal_history
                ),
                contentDescription = null,
                Modifier
                    .clip(CircleShape)
                    .clickable {
                        isMealHistoryExpanded.value = !isMealHistoryExpanded.value
                    }
                    .rotate(rotationAngle.value)
            )
        }
    }
    if (isDialogActive.value) {
        PickDialog(
            isDialogActive = isDialogActive,
            onCreateNewRecipe = onCreateNewRecipe,
            onAddMeal = onAddMeal,
            onManageLocalFoodDb = onManageLocalFoodDb
        )
    }
}

@Composable
private fun NutrientText(
    modifier: Modifier = Modifier,
    nutrientType: String,
    nutrientCount: Int
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = nutrientType)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = nutrientCount.toString())
    }
}

@Composable
private fun MealHistoryItem(
    modifier: Modifier = Modifier,
    time: String,
    name: String,
    mass: Int,
    cals: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = time, Modifier.weight(.2f))
        Text(text = "$name, $mass g", Modifier.weight(1f))
        Text(
            text = "$cals cal",
            Modifier.weight(.25f),
            textAlign = TextAlign.End
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PickDialog(
    modifier: Modifier = Modifier,
    isDialogActive: MutableState<Boolean>,
    onCreateNewRecipe: () -> Unit,
    onAddMeal: () -> Unit,
    onManageLocalFoodDb: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = {isDialogActive.value = false}) {
        Card(
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DialogItem(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.new_recipe,
                    text = "New recipe",
                    onClick = {
                        isDialogActive.value = false
                        onCreateNewRecipe()
                    }
                )
                DialogItem(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.add_meal,
                    text = "Add meal",
                    onClick = {
                        isDialogActive.value = false
                        onAddMeal()
                    }
                )
                DialogItem(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.manage_food_db,
                    text = "Food manager",
                    onClick = {
                        isDialogActive.value = false
                        onManageLocalFoodDb()
                    }
                )
            }
        }
    }
}

@Composable
private fun DialogItem(
    modifier: Modifier = Modifier,
    iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )
        }
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun Preview() {
    PickDialog(
        isDialogActive = mutableStateOf(true),
        onCreateNewRecipe = { /*TODO*/ },
        onAddMeal = { /*TODO*/ }) {

    }
}