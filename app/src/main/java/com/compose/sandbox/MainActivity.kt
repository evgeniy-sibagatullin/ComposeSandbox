package com.compose.sandbox

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.sandbox.MainActivity.Companion.COLUMNS
import com.compose.sandbox.MainActivity.Companion.ROWS
import com.compose.sandbox.ui.theme.SandboxTheme
import java.util.*

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

private val counterStates = mutableListOf<MutableList<MutableState<CellType?>>>()

private var isRedMove: Boolean = true

private val newCellType: CellType
    get() = if (isRedMove) CellType.RED else CellType.WHITE

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
    for (columnIndex in 0 until COLUMNS) {
        val columnState = mutableListOf<MutableState<CellType?>>()
        counterStates.add(columnState)

        for (rowIndex in 0 until ROWS) {
            columnState.add(remember { mutableStateOf(null) })
        }
    }

    Column(verticalArrangement = Arrangement.Bottom) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${newCellType.name.toLowerCase(Locale.getDefault())} move",
                color = Color.Yellow,
                fontSize = 30.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) { Cell(cellType = newCellType, columnIndex = -1) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Yellow)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (columnIndex in 0 until COLUMNS) {
                val columnState = counterStates[columnIndex]

                Column {
                    for (rowIndex in 0 until ROWS) {
                        val state = columnState[rowIndex]
                        Cell(state.value, columnIndex)
                    }
                }
            }
        }
    }
}

@Composable
private fun Cell(cellType: CellType?, columnIndex: Int) {
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
                .background(Color.Blue)
                .clickable(cellType == null) {
                    fillCellForColumn(columnIndex)
                }) {
            if (cellType != null) CellFilling(cellType = cellType)
        }
    }
}

private fun fillCellForColumn(columnIndex: Int) {
    for (i in (ROWS - 1) downTo 0) {
        if (counterStates[columnIndex][i].value == null) {
            counterStates[columnIndex][i].value = newCellType
            break
        }
    }

    isRedMove = !isRedMove
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