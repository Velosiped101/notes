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
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = programList[index.value].exercise)
            Text(text = "Planned - ${programList[index.value].reps} reps")
            repeat(programList[index.value].sets) {
                val sliderValue = remember {
                    mutableFloatStateOf(0f)
                }
                Column() {
                    if (programList[index.value].sets > 1)
                        Text(text = "Set ${it+1}: ${sliderValue.floatValue.toInt()} reps done.")
                    Slider(
                        value = sliderValue.floatValue,
                        onValueChange = {sliderValue.floatValue = it},
                        valueRange = 0f..programList[index.value].reps.toFloat()
                    )
                }
            }
        }
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

@Preview (showSystemUi = true)
@Composable
private fun Preview() {
    ProgramExecScreen(programList = emptyList<Program>())
}