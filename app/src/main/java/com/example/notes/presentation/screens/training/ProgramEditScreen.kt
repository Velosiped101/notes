package com.example.notes.presentation.screens.training

import android.annotation.SuppressLint
import android.graphics.Paint.Style
import android.inputmethodservice.Keyboard.Row
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.data.local.program.Exercise
import com.example.notes.data.local.program.Program
import com.example.notes.ui.theme.GreenDay
import com.example.notes.utils.DayHolder
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.DayState
import com.example.notes.utils.DayType
import kotlin.math.exp

@Composable
fun ProgramEditScreen(
    dayHolderState: MutableState<DayHolder<DayState>>,
    onClick: () -> Unit
) {
    Scaffold(topBar = { ProgramTopBar(onClick = onClick) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val dummyDay = remember {
                mutableStateOf("Rest")
            }
            DayPicker(
                onClick = {},
                dayHolderState = dayHolderState
            )
            when (dayHolderState.value) {
                is DayHolder.Friday -> ProgramFragment(
                    day = dummyDay,
                    program = dayHolderState.value.fridayState?.program ?: emptyList()
                )
                is DayHolder.Monday -> ProgramFragment(
                    day = dummyDay,
                    program = dayHolderState.value.mondayState?.program ?: emptyList()
                )
                is DayHolder.Saturday -> ProgramFragment(
                    day = dummyDay,
                    program = dayHolderState.value.saturdayState?.program ?: emptyList()
                )
                is DayHolder.Sunday -> ProgramFragment(
                    day = dummyDay,
                    program = dayHolderState.value.sundayState?.program ?: emptyList()
                )
                is DayHolder.Thursday -> ProgramFragment(
                    day = dummyDay,
                    program = dayHolderState.value.thursdayState?.program ?: emptyList()
                )
                is DayHolder.Tuesday -> ProgramFragment(
                    day = dummyDay,
                    program = dayHolderState.value.tuesdayState?.program ?: emptyList()
                )
                is DayHolder.Wednesday -> ProgramFragment(
                    day = dummyDay,
                    program = dayHolderState.value.wednesdayState?.program ?: emptyList()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgramTopBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Customize your program") },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onClick() }
            )
        }
    )
}

@Composable
private fun DayPicker(
    onClick: () -> Unit,
    dayHolderState: MutableState<DayHolder<DayState>>
) {
    val days = DayOfWeek.entries
    val gradient = Brush.verticalGradient(
        colors = listOf(GreenDay, Color.Transparent),
        startY = Float.POSITIVE_INFINITY,
        endY = 100.0f
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val pickedDay = remember {
                mutableStateOf(DayOfWeek.Monday)
            }
            days.forEach { day ->
                val modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable {
                        onClick()
                        dayHolderState.value = DayHolder.getDayState(day)
                        pickedDay.value = day
                    }
                DayText(
                    text = day.name.slice(0..2),
                    modifier = if (day == pickedDay.value) {
                        modifier.drawWithCache {
                            onDrawBehind {
                                drawRect(
                                    brush = gradient
                                )
                            }
                        }
                    } else {
                        modifier
                    }
                )
                if (day != days.last()) VerticalDivider()
            }
        }
    }
}

@Composable
private fun DayText(
    text: String,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun ProgramFragment(
    day: MutableState<String>,
    program: List<Program>
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = day.value,
            Modifier.clickable {
                expanded.value = true
            },
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {  },
            modifier = Modifier.fillMaxWidth()
        ) {
            DayType.entries.forEach() {
                DropdownMenuItem(
                    text = { Text(text = it.toString()) },
                    onClick = { expanded.value = false; day.value = it.toString() }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProgramRow(
            firstColumn = "Exercise",
            secondColumn = "Sets",
            thirdColumn = "Reps"
        )
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(program) {
                ProgramRow(
                    firstColumn = it.exercise,
                    secondColumn = it.sets.toString(),
                    thirdColumn = it.reps.toString()
                )
            }
        }
    }
}

@Composable
fun ProgramRow(
    firstColumn: String,
    secondColumn: String,
    thirdColumn: String
) {
    val style = TextStyle(
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = firstColumn, Modifier.weight(1f), style = style)
        Text(text = secondColumn, Modifier.weight(.2f), style = style)
        Text(text = thirdColumn, Modifier.weight(.2f), style = style)
    }
}

private val dummy = mutableListOf<Program>(
    Program(dayOfWeek = 1, exercise = "kushat", sets = 4, reps = 10),
    Program(dayOfWeek = 1, exercise = "spat", sets = 2, reps = 3),
    Program(dayOfWeek = 1, exercise = "youtube", sets = 4, reps = 4),
    Program(dayOfWeek = 1, exercise = "android studio", sets = 5, reps = 8),
    Program(dayOfWeek = 1, exercise = "gato", sets = 5, reps = 3)
)

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    ProgramFragment(day = mutableStateOf("Rest"), program = dummy)
}