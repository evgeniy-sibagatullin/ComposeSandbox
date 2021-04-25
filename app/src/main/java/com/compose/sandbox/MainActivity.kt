package com.compose.sandbox

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

private var isRedMove: Boolean = true

@Composable
fun MyApp(content: @Composable () -> Unit) {
    SandboxTheme {
        Surface(color = Color.Blue, modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
private fun MyScreenContent() {
    val counterStates = mutableListOf<MutableList<MutableState<CellType?>>>()

    for (columnIndex in 0 until COLUMNS) {
        val columnState = mutableListOf<MutableState<CellType?>>()
        counterStates.add(columnState)

        for (rowIndex in 0 until ROWS) {
            columnState.add(remember { mutableStateOf(null) })
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
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
                        Cell(state.value) { state.value = it }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}

@Composable
private fun Cell(cellType: CellType?, fillCell: (CellType?) -> Unit) {
    Column(
        modifier = Modifier
            .size(48.dp)
            .padding(8.dp)
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Green)
                .clickable(cellType == null) {
                    val newCellType = if (isRedMove) CellType.RED else CellType.WHITE
                    fillCell(newCellType)
                    isRedMove = !isRedMove
                }) {
            if (cellType != null) CellFilling(cellType = cellType)
        }
    }
}

@Composable
private fun CellFilling(cellType: CellType) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(6.dp)
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(cellType.color)
        ) {}
    }
}

enum class CellType(val color: Color) {
    RED(Color.Red), WHITE(Color.White)
}