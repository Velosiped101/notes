package com.example.notes.presentation.screens.diet

import android.util.Log
import android.widget.Toast
import androidx.collection.MutableIntList
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Delete
import com.example.notes.R
import com.example.notes.data.local.food.Food
import com.example.notes.presentation.screens.diet.components.FoodCardItem
import com.example.notes.ui.theme.Typography

@Composable
fun FoodManagerScreen(
    onAddButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onFoodItemClicked: (Food) -> Unit,
    onLongClick: () -> Unit,
    foodList: List<Food>,
    isInDeleteMode: MutableState<Boolean>,
    onSelectedForDelete: (Food) -> Unit,
    selectedItems: MutableList<Food>,
    onDelete: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TopBar(
                onAddButtonClicked = { onAddButtonClicked() },
                onBackButtonClicked = { onBackButtonClicked() },
                isInDeleteMode = isInDeleteMode.value,
                onDelete = onDelete
            )
            LazyColumn {
                items(foodList) { item ->
                    val colors = if (selectedItems.contains(item) && isInDeleteMode.value)
                        CardDefaults.cardColors(containerColor = Color.Red)
                    else CardDefaults.cardColors(containerColor = Color.White)
                    FoodCardItem(
                        food = item,
                        onFoodItemClicked = {
                            if (!isInDeleteMode.value) onFoodItemClicked(item)
                            else onSelectedForDelete(item)
                        },
                        onLongClick = {
                            onLongClick()
                            onSelectedForDelete(item)
                        },
                        colors = colors
                    )
                }
            }
        }
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
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null
            )
        },
        actions = {
            IconButton(onClick = { onAddButtonClicked() }) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = null
                )
            }
            if (isInDeleteMode)
                IconButton(onClick = onDelete) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = null
                    )
                }
        }
    )
}