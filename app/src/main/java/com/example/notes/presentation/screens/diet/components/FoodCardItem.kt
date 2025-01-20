package com.example.notes.presentation.screens.diet.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.size.Dimension
import coil3.toCoilUri
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
            .combinedClickable(onLongClick = onLongClick) {
                onFoodItemClicked()
            },
        shape = MaterialTheme.shapes.medium,
        colors = colors,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isLoading = remember {
                mutableStateOf(false)
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ){
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(food.imageUrl)
                        .build(),
                    contentDescription = null,
                    onLoading = { isLoading.value = true },
                    onError = {  },
                    onSuccess = { isLoading.value = false }
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = food.name, style = Typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "P - ${food.protein} g " +
                            "| F - ${food.fat} g " +
                            "| C - ${food.carbs} g",
                    style = Typography.titleMedium
                )
            }
        }
    }
}