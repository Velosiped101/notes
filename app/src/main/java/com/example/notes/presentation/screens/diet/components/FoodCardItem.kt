package com.example.notes.presentation.screens.diet.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notes.data.local.food.Food
import com.example.notes.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodCardItem(
    food: Food,
    onFoodItemClicked: () -> Unit,
    onLongClick: () -> Unit,
    colors: CardColors
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(onLongClick = onLongClick) { onFoodItemClicked() },
        shape = MaterialTheme.shapes.medium,
        colors = colors,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = food.foodName, style = Typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${food.protein} | ${food.fat} | ${food.carbs}",
                    style = Typography.titleMedium
                )
            }
        }
    }
}