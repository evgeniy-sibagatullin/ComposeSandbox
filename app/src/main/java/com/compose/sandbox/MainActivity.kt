package com.compose.sandbox

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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

class MainActivity : AppCompatActivity() {
    companion object {
        const val COLUMNS: Int = 7
        const val ROWS: Int = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { MyApp() }
    }
}

private enum class CellType(val color: Color) {
    RED(Color.Red), WHITE(Color.White)
}

private val cellStates = mutableListOf<MutableList<MutableState<CellType?>>>()

private val newCellType: CellType
    get() = if (isRedMove) CellType.RED else CellType.WHITE

private var isRedMove: Boolean = true

private var isGameFinished: Boolean = false

@Composable
private fun MyApp() {
    SetupCellStates()

    SandboxTheme {
        Surface(color = Color.Blue, modifier = Modifier.fillMaxSize()) {
            MyScreenContent()
        }
    }
}

@Composable
private fun SetupCellStates() {
    for (columnIndex in 0 until COLUMNS) {
        val columnState = mutableListOf<MutableState<CellType?>>()
        cellStates.add(columnState)

        for (rowIndex in 0 until ROWS) {
            columnState.add(remember { mutableStateOf(null) })
        }
    }
}

@Composable
private fun MyScreenContent() {
    Column(verticalArrangement = Arrangement.Bottom) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isGameFinished) {
                Button(
                    onClick = { restartGame() },
                    colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Yellow)
                ) {
                    Text(text = "Restart", color = Color.Blue, fontSize = 30.sp)
                }
            } else {
                val text = "${newCellType.name.lowercase()} move"
                Text(text = text, color = Color.Yellow, fontSize = 30.sp)
            }
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
                val columnState = cellStates[columnIndex]

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
                    if (!isGameFinished) {
                        makeMove(columnIndex)
                    }
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

private fun makeMove(columnIndex: Int) {
    for (i in (ROWS - 1) downTo 0) {
        if (cellStates[columnIndex][i].value == null) {
            cellStates[columnIndex][i].value = newCellType

            if (isWinnerMove(i, columnIndex)) {
                isGameFinished = true
            } else {
                isRedMove = !isRedMove
            }

            break
        }
    }
}

private fun isWinnerMove(row: Int, column: Int): Boolean {
    if (sameCellsBySide(row, column, 1, 0) > 2) return true
    if (sameCellsBySide(row, column, 0, -1) + sameCellsBySide(row, column, 0, 1) > 2) return true
    if (sameCellsBySide(row, column, -1, -1) + sameCellsBySide(row, column, 1, 1) > 2) return true
    if (sameCellsBySide(row, column, 1, -1) + sameCellsBySide(row, column, -1, 1) > 2) return true
    return false
}

private fun sameCellsBySide(row: Int, column: Int, rowShift: Int, columnShift: Int): Int {
    var count = 0
    var columnIndex = column + columnShift
    var rowIndex = row + rowShift

    while (columnIndex in 0 until COLUMNS && rowIndex in 0 until ROWS &&
        cellStates[columnIndex][rowIndex].value == newCellType
    ) {
        count++
        columnIndex += columnShift
        rowIndex += rowShift
    }

    return count
}

private fun restartGame() {
    for (columnIndex in 0 until COLUMNS) {
        for (rowIndex in 0 until ROWS) {
            cellStates[columnIndex][rowIndex].value = null
        }
    }

    isGameFinished = false
    isRedMove = !isRedMove
}