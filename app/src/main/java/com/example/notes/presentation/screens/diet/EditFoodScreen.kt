package com.example.notes.presentation.screens.diet

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes.R

@Composable
fun EditFoodScreen(
    onBackButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
    foodItemState: FoodItemState
) {
    val focusManager = LocalFocusManager.current
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TopBar(
                onSaveButtonClicked = onSaveButtonClicked,
                onBackButtonClicked = onBackButtonClicked,
                isCompleted = foodItemState.nameState.value.isNotEmpty() &&
                        foodItemState.proteinState.value.isNotEmpty() &&
                        foodItemState.fatState.value.isNotEmpty() &&
                        foodItemState.carbsState.value.isNotEmpty()
            )
            InputField(
                text = "Name",
                placeholder = "Mazzafegato",
                foodItemState.nameState,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )
            InputField(
                text = "Protein",
                placeholder = "25",
                foodItemState.proteinState,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )
            InputField(
                text = "Fat",
                placeholder = "22",
                foodItemState.fatState,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )
            InputField(
                text = "Carbs",
                placeholder = "4",
                foodItemState.carbsState,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onSaveButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    isCompleted: Boolean
) {
    TopAppBar(
        title = {
            Text(text = "Food editor")
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBackButtonClicked() },
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null
            )
        },
        actions = {
            AnimatedVisibility(
                visible = isCompleted
                ){
                IconButton(onClick = onSaveButtonClicked) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.baseline_done_24),
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
private fun InputField(
    text: String,
    placeholder: String,
    inputText: MutableState<String>,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(.5f)
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(100),
            value = inputText.value,
            onValueChange = { inputText.value = it },
            keyboardOptions = keyboardOptions,
            singleLine = true,
            placeholder = { Text(text = placeholder, Modifier.alpha(.5f))},
            keyboardActions = keyboardActions
        )
    }
}