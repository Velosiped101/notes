package com.example.notes.presentation.screens.diet

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.data.local.food.Food

@Composable
fun NewRecipeScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onConfirmButtonClicked: (Food) -> Unit
) {
    val isDialogActive = remember {
        mutableStateOf(false)
    }
    val emptyIngredient = Ingredient(
        name = "",
        protein = 0,
        fat = 0,
        carbs = 0,
        mass = 0,
        saveToDb = false
    )
    val ingredientsList = remember {
        mutableStateListOf(
            emptyIngredient
        )
    }
    val completeMass = remember {
        mutableStateOf(
            ingredientsList.sumOf { it.mass }
        )
    }
    val recipeName = remember {
        mutableStateOf("")
    }
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NewRecipeTopBar(
                onBackButtonClicked = onBackButtonClicked,
                onConfirmButtonClicked = {
                    if (completeMass.value == 0) isDialogActive.value = true
                    else {
                        val totalMass = ingredientsList.sumOf { it.mass }
                        val totalProtein = ingredientsList.sumOf { it.protein * it.mass/100 } * totalMass/completeMass.value
                        val totalFat = ingredientsList.sumOf { it.fat * it.mass/100 } * totalMass/completeMass.value
                        val totalCarbs = ingredientsList.sumOf { it.carbs * it.mass/100 } * totalMass/completeMass.value
                        onConfirmButtonClicked(
                            Food(
                                foodName = recipeName.value,
                                protein = totalProtein,
                                fat = totalFat,
                                carbs = totalCarbs
                            )
                        )
                    }
                },
                onWeighingClicked = { isDialogActive.value = true },
                mass = completeMass
            )
            IngredientsHeader()
            IngredientsTable(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .weight(1f),
                ingredients = ingredientsList
            )
            IngredientsCounter(
                onMinus = { if (ingredientsList.size > 1) ingredientsList.removeAt(ingredientsList.lastIndex) },
                onPlus = { ingredientsList.add(emptyIngredient) },
                numberOfIngredients = ingredientsList.size
            )
        }
    }
    if (isDialogActive.value) MassDialog(
        isDialogActive = isDialogActive,
        mass = completeMass,
        name = recipeName
    )
}

@Composable
private fun IngredientsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val style = TextStyle(textAlign = TextAlign.Center, fontSize = 13.sp)
        Text(text = "Ingredient", modifier = Modifier.weight(1f), style = style)
        Text(text = "Protein", modifier = Modifier.weight(.25f), style = style)
        Text(text = "Fat", modifier = Modifier.weight(.25f), style = style)
        Text(text = "Carbs", modifier = Modifier.weight(.25f), style = style)
        Text(text = "Mass", modifier = Modifier.weight(.25f), style = style)
        Text(text = "Save to DB", modifier = Modifier.weight(.25f), style = style)
    }
}

@Composable
fun IngredientsCounter(
    modifier: Modifier = Modifier,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    numberOfIngredients: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp, top = 12.dp)
    ) {
        IconButton(onClick = onMinus, modifier = Modifier.size(15.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.minus),
                contentDescription = null
            )
        }
        Text(
            text = numberOfIngredients.toString(),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        IconButton(onClick = onPlus, modifier = Modifier.size(15.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.add_plus),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MassDialog(
    modifier: Modifier = Modifier,
    isDialogActive: MutableState<Boolean>,
    mass: MutableState<Int>,
    name: MutableState<String>
) {
    val massText = remember {
        mutableStateOf("")
    }
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
                Text(text = "Name")
                Spacer(modifier = Modifier.height(12.dp))
                InputField(state = name, modifier = Modifier, onValueChange = {})
                Text(text = "Mass of the food")
                Spacer(modifier = Modifier.height(12.dp))
                InputField(state = massText, modifier = Modifier, onValueChange = {})
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isDialogActive.value = false }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        isDialogActive.value = false
                        mass.value = massText.value.toInt()
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
private fun IngredientsTable(
    modifier: Modifier,
    ingredients: SnapshotStateList<Ingredient>
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(ingredients) { id, ingredient ->
            val name = remember {
                mutableStateOf(ingredient.name)
            }
            val protein = remember {
                mutableStateOf(ingredient.protein.toString())
            }
            val fat = remember {
                mutableStateOf(ingredient.fat.toString())
            }
            val carbs = remember {
                mutableStateOf(ingredient.carbs.toString())
            }
            val mass = remember {
                mutableStateOf(ingredient.mass.toString())
            }
            val saveToDb = remember {
                mutableStateOf(ingredient.saveToDb)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputField(
                    state = name,
                    modifier = Modifier.weight(1f),
                    onValueChange = { ingredients[id] = ingredient.copy(name = name.value) }
                )
                InputField(
                    state = protein,
                    modifier = Modifier.weight(.25f),
                    onValueChange = {
                        if (protein.value.isNotEmpty())
                            ingredients[id] = ingredient.copy(protein = protein.value.toInt())
                        else
                            ingredients[id] = ingredient.copy(protein = 0)
                    }
                )
                InputField(
                    state = fat,
                    modifier = Modifier.weight(.25f),
                    onValueChange = {
                        if (fat.value.isNotEmpty())
                            ingredients[id] = ingredient.copy(fat = fat.value.toInt())
                        else
                            ingredients[id] = ingredient.copy(fat = 0)
                    }
                )
                InputField(
                    state = carbs,
                    modifier = Modifier.weight(.25f),
                    onValueChange = {
                        if (carbs.value.isNotEmpty())
                            ingredients[id] = ingredient.copy(carbs = carbs.value.toInt())
                        else
                            ingredients[id] = ingredient.copy(carbs = 0)
                    }
                )
                InputField(
                    state = mass,
                    modifier = Modifier.weight(.25f),
                    onValueChange = {
                        if (mass.value.isNotEmpty())
                            ingredients[id] = ingredient.copy(mass = mass.value.toInt())
                        else
                            ingredients[id] = ingredient.copy(mass = 0)
                    }
                )
                Checkbox(
                    checked = saveToDb.value,
                    onCheckedChange = {
                        saveToDb.value = it
                        ingredients[id] = ingredient.copy(saveToDb = it)
                    },
                    modifier = Modifier
                        .weight(.25f)
                        .size(30.dp)
                )
            }
        }
    }
}

@Composable
private fun InputField(
    state: MutableState<String>,
    modifier: Modifier,
    onValueChange: () -> Unit
) {
    BasicTextField(
        value = state.value,
        modifier = modifier,
        onValueChange = {
            state.value = it
            onValueChange()
        },
        decorationBox = { inputField ->
            Card(
                border = BorderStroke(1.dp, Color.Black),
                shape = RectangleShape,
                modifier = Modifier
                    .height(30.dp)
                    .padding(1.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                inputField()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewRecipeTopBar(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onConfirmButtonClicked: () -> Unit,
    onWeighingClicked: () -> Unit,
    mass: MutableState<Int>
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(text = "New recipe")
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onBackButtonClicked()
                    },
                painter = painterResource(id = R.drawable.back),
                contentDescription = null
            )
        },
        actions = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onWeighingClicked()
                }
            ) {
                Text(text = mass.value.toString())
                Icon(
                    modifier = Modifier
                        .size(40.dp),
                    painter = painterResource(id = R.drawable.weighing_machine),
                    contentDescription = null
                )
            }
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onConfirmButtonClicked()
                    },
                painter = painterResource(id = R.drawable.confirm),
                contentDescription = null
            )
        }
    )
}

private data class Ingredient(
    var name: String,
    var protein: Int,
    var fat: Int,
    var carbs: Int,
    var mass: Int,
    var saveToDb: Boolean
)

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun Preview() {
    NewRecipeScreen(onBackButtonClicked = { /*TODO*/ }) {

    }
}