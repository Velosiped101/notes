package com.example.notes.presentation.screens.diet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.ui.theme.Typography

@Composable
fun DietInfoScreen(
    onAddMealClicked: () -> Unit,
    onManageFoodClicked: () -> Unit,
    onBackButtonClicked: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TopBar(onBackButtonClicked)
            ChoiceCard(
                text = "Add meal",
                icon = R.drawable.baseline_add_24,
                onChoiceClicked = onAddMealClicked
            )
            ChoiceCard(
                text = "Manage food information",
                icon = R.drawable.baseline_dataset_24,
                onChoiceClicked = onManageFoodClicked
            )
        }
    }
}

@Composable
fun ChoiceCard(
    onChoiceClicked: () -> Unit,
    text: String,
    icon: Int
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onChoiceClicked() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = Typography.titleLarge
            )
            Icon(
                modifier = Modifier
                    .size(40.dp),
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackButtonClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "What would you like to do?")
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBackButtonClicked() },
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PrevDietInfoScreen() {
    DietInfoScreen({}, {}) {}
}