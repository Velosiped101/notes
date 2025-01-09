package com.example.notes.presentation.screens.training

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.example.notes.utils.DayOfWeek
import com.example.notes.utils.ExerciseType

@Composable
fun ProgramEditScreen(
    programList: List<Program>,
    onBackClick: () -> Unit,
    exerciseList: List<Exercise>,
    onClick: (Program) -> Unit,
    onDelete: (Program) -> Unit,
) {
    val dayOfWeek = remember {
        mutableStateOf(DayOfWeek.Monday)
    }
    BackHandler {
        onBackClick()
    }
    Scaffold(topBar = {
        ProgramTopBar(onClick = {
            onBackClick()
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            DayPicker(
                dayOfWeek = dayOfWeek
            )
            ProgramFragment(
                program = programList.filter { it.dayOfWeek == dayOfWeek.value.name },
                exerciseList = exerciseList,
                onClick = onClick,
                onDelete = onDelete,
                dayOfWeek = dayOfWeek
            )
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
                painter = painterResource(id = R.drawable.back),
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
    dayOfWeek: MutableState<DayOfWeek>,
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
            days.forEach { day ->
                val modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable {
                        dayOfWeek.value = day
                    }
                DayText(
                    text = day.name.slice(0..2),
                    modifier = if (day == dayOfWeek.value) {
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
    modifier: Modifier,
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
    program: List<Program>,
    exerciseList: List<Exercise>,
    onClick: (Program) -> Unit,
    onDelete: (Program) -> Unit,
    dayOfWeek: MutableState<DayOfWeek>,
) {
    val isDialogActive = remember {
        mutableStateOf(false)
    }
    val pickedProgramItem = remember {
        mutableStateOf<Program?>(null)
    }
    if (program.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProgramRow(
                exercise = "Exercise",
                reps = "Reps",
                onClick = { }
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(program) {
                    ProgramRow(
                        exercise = it.exercise,
                        reps = it.reps.toString(),
                        onClick = {
                            isDialogActive.value = true
                            pickedProgramItem.value = it
                        }
                    )
                }
            }
            IconButton(
                onClick = {
                    isDialogActive.value = true
                    pickedProgramItem.value = null
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_plus),
                    contentDescription = null
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Rest day")
            Text(
                text = "Add exercise",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clickable {
                        isDialogActive.value = true
                        pickedProgramItem.value = null
                    }
            )
        }
    }
    if (isDialogActive.value) ExercisePickDialog(
        isDialogActive = isDialogActive,
        exerciseList = exerciseList,
        onClick = onClick,
        onDelete = onDelete,
        dayOfWeek = dayOfWeek,
        programItem = pickedProgramItem.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePickDialog(
    isDialogActive: MutableState<Boolean>,
    exerciseList: List<Exercise>,
    onClick: (Program) -> Unit,
    onDelete: (Program) -> Unit,
    dayOfWeek: MutableState<DayOfWeek>,
    programItem: Program?
) {
    BackHandler(
        enabled = programItem != null
    ) {
        isDialogActive.value = false
    }
    val isExpanded = remember {
        mutableStateOf(false)
    }
    val pickedType = remember {
        mutableStateOf(ExerciseType.All)
    }
    val filteredExerciseList = remember {
        mutableStateOf(exerciseList)
    }
    val isSettingSetsAndReps = remember {
        mutableStateOf(
            programItem != null
        )
    }
    val pickedExerciseName = remember {
        mutableStateOf(programItem?.exercise ?: "")
    }
    BasicAlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.6f),
        onDismissRequest = { isDialogActive.value = false }
    ) {
        Card(
            border = BorderStroke(1.dp, SolidColor(Color.Black))
        ) {
            if (!isSettingSetsAndReps.value) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Pick an exercise")
                        Column {
                            Text(
                                text = pickedType.value.name,
                                modifier = Modifier.clickable { isExpanded.value = true }
                            )
                            DropdownMenu(
                                expanded = isExpanded.value,
                                onDismissRequest = { isExpanded.value = false }
                            ) {
                                ExerciseType.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(text = type.name) },
                                        onClick = {
                                            pickedType.value = type
                                            if (pickedType.value != ExerciseType.All)
                                                filteredExerciseList.value = exerciseList.filter {
                                                    it.type == pickedType.value.name
                                                }
                                            else filteredExerciseList.value = exerciseList
                                            isExpanded.value = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider()
                    LazyColumn {
                        items(filteredExerciseList.value) {
                            Text(text = it.name,
                                Modifier.clickable {
                                    pickedExerciseName.value = it.name
                                    isSettingSetsAndReps.value = true
                                }
                            )
                        }
                    }

                }
            } else {
                BackHandler {
                    isSettingSetsAndReps.value = false
                }
                SetsAndRepsSetter(
                    pickedExerciseName = pickedExerciseName.value,
                    dayOfWeek = dayOfWeek.value.name,
                    onConfirm = {
                        isDialogActive.value = false
                        onClick(it)
                    },
                    onBack = {
                        isSettingSetsAndReps.value = false
                        if (programItem != null) isDialogActive.value = false
                    },
                    onDelete = {
                        onDelete(it)
                        isDialogActive.value = false
                    },
                    programItem = programItem
                )
            }
        }
    }
}

@Composable
fun SetsAndRepsSetter(
    modifier: Modifier = Modifier,
    pickedExerciseName: String,
    onConfirm: (Program) -> Unit,
    onBack: () -> Unit,
    onDelete: (Program) -> Unit,
    dayOfWeek: String,
    programItem: Program?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        val sets = remember {
            mutableFloatStateOf(1f)
        }
        val reps = remember {
            mutableFloatStateOf(programItem?.reps?.toFloat() ?: 1f)
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = pickedExerciseName, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(12.dp))
                if (programItem == null) {
                    Text(text = "Sets - ${sets.floatValue.toInt()}")
                    SetterSlider(type = sets, range = 1f..5f)
                }
                Text(text = "Reps per set - ${reps.floatValue.toInt()}")
                SetterSlider(type = reps, range = 1f..20f)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null
                    )
                }
                if (programItem != null)
                    IconButton(onClick = {
                        onDelete(programItem)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.delete), contentDescription = null)
                    }
                IconButton(onClick = {
                    repeat(sets.floatValue.toInt()) {
                        onConfirm(
                            programItem?.copy(
                                reps = reps.floatValue.toInt()
                            ) ?: Program(
                                dayOfWeek = dayOfWeek,
                                exercise = pickedExerciseName,
                                reps = reps.floatValue.toInt()
                            )
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetterSlider(
    type: MutableFloatState,
    range: ClosedFloatingPointRange<Float>
) {
    Slider(
        value = type.floatValue,
        onValueChange = { type.floatValue = it },
        valueRange = range,
        track = {
            SliderDefaults.Track(
                sliderState = it,
                colors = SliderDefaults.colors(
                    inactiveTrackColor = Color.Gray,
                    activeTrackColor = Color.Black,
                ),
                modifier = Modifier.height(2.dp)
            )
        },
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color.Black
                )
            )
        }
    )
}

@Composable
fun ProgramRow(
    exercise: String,
    reps: String,
    onClick: (() -> Unit)
) {
    val style = TextStyle(
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = exercise, Modifier.weight(1f), style = style)
        Text(text = reps, Modifier.weight(.2f), style = style)
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun Preview() {

}

private val dummy = listOf(
    Exercise(name = "twitch", type = "fun"),
    Exercise(name = "youtube", type = "fun"),
    Exercise(name = "android studio", type = "work")
)