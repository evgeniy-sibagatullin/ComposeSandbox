package com.compose.sandbox

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.compose.sandbox.MainActivity.Companion.COLUMNS
import com.compose.sandbox.MainActivity.Companion.ROWS
import com.compose.sandbox.ui.theme.SandboxTheme

class MainActivity : AppCompatActivity() {

    companion object {
        const val COLUMNS: Int = 7
        const val ROWS: Int = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MyScreenContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    SandboxTheme {
        Surface(color = Color.Blue, modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun MyScreenContent() {
    val counterStates = mutableListOf<MutableList<MutableState<Int>>>()

    for (columnIndex in 0 until COLUMNS) {
        val columnState = mutableListOf<MutableState<Int>>()
        counterStates.add(columnState)

        for (rowIndex in 0 until ROWS) {
            columnState.add(remember { mutableStateOf(0) })
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        for (columnIndex in 0 until COLUMNS) {
            val columnState = counterStates[columnIndex]

            Column {
                for (rowIndex in 0 until ROWS) {
                    val state = columnState[rowIndex]
                    Counter(state.value) { state.value = it }
                }
            }
        }
    }
}

@Composable
fun Counter(count: Int, updateCount: (Int) -> Unit) {
    Box(modifier = Modifier
        .size(50.dp)
        .padding(8.dp)
        .clickable {
            updateCount(count + 1)
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Yellow),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$count")
        }
    }
}