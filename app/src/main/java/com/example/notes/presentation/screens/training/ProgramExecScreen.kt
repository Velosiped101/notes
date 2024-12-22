package com.example.notes.presentation.screens.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.notes.data.local.program.Program
import kotlin.math.max

@Composable
fun ProgramExecScreen(
    modifier: Modifier = Modifier,
    programList: List<Program>
) {
    val index = remember {
        mutableStateOf(0)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ProgramFragment(programItem = programList[index.value])
        Text(
            text = "next",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { if (index.value < programList.lastIndex) index.value++ }
        )
        Text(
            text = "previous",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { if (index.value > 0) index.value-- }
        )
    }
}

@Composable
fun ProgramFragment(
    programItem: Program
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = programItem.exercise)
        Text(text = "Planned - ${programItem.reps} reps")
        Column() {
            val sliderValue = remember {
                mutableFloatStateOf(0f)
            }
            Slider(
                value = sliderValue.floatValue,
                onValueChange = { sliderValue.floatValue = it },
                valueRange = 0f..programItem.reps.toFloat()
            )
        }
    }
}

@Preview (showSystemUi = true)
@Composable
private fun Preview() {
    ProgramExecScreen(programList = emptyList<Program>())
}