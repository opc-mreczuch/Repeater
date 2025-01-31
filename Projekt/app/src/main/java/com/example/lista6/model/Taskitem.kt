package com.example.lista6.ui.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lista6.model.Task


// Działanie Zadań, czyli przesuwanie, zaznaczanie i ogólny wygląd
@Composable
fun TaskItem(
    task: Task,
    tasks: List<Task>,
    onTaskClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMove: (from: Int, to: Int) -> Unit,
    index: Int,
    taskHeight: Int = 50
) {
    var offsetY by remember { mutableStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        offsetY += delta
    }
    val backgroundColor = if (task.isDone) Color(0xFFD0DDD0) else Color(0xFFF9F7F7) // Kolor karty #D0DDD0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .offset(y = offsetY.dp)
            .draggable(
                state = draggableState,
                orientation = Orientation.Vertical,
                onDragStarted = { offsetY = 0f },
                onDragStopped = {
                    val newIndex = index + (offsetY / taskHeight).toInt()
                    val clampedIndex = newIndex.coerceIn(0, tasks.size - 1)
                    if (clampedIndex != index) {
                        onMove(index, clampedIndex)
                    }
                    offsetY = 0f
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = {
                    onTaskClick()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF578E7E),
                    uncheckedColor = Color(0xFF578E7E)
                )
            )

            Text(
                text = task.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Usuń zadanie",
                    tint = Color.Red
                )
            }
        }
    }
}