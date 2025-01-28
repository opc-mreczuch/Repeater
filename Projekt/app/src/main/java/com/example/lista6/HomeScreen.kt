package com.example.lista6.ui.screens

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.lista6.model.Task
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var taskName by remember { mutableStateOf("") }
    var isAddingTask by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) } // Zadanie do usunięcia (jeśli użytkownik wybierze usunięcie)
    val coroutineScope = rememberCoroutineScope()

    // Funkcja do dodania nowego zadania
    fun addTask() {
        if (taskName.isNotEmpty()) {
            tasks = tasks + Task(name = taskName) // Dodanie zadania do listy
            taskName = "" // Resetujemy nazwę zadania
        }
    }

    // Funkcja do usunięcia zadania
    fun deleteTask(task: Task) {
        tasks = tasks.filter { it != task } // Usunięcie zadania z listy
        taskToDelete = null // Resetowanie zadania do usunięcia
    }

    // Funkcja do zmiany kolejności zadań
    fun moveTask(from: Int, to: Int) {
        val updatedTasks = tasks.toMutableList()
        val task = updatedTasks.removeAt(from)
        updatedTasks.add(to, task)
        tasks = updatedTasks
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Wyświetlanie przycisku "Dodaj zadanie" na samej górze ekranu
        Button(
            onClick = { isAddingTask = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dodaj zadanie")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Spacer dla oddzielenia przycisku od formularza

        // Wyświetlanie listy zadań
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(tasks.size) { index ->
                val task = tasks[index]
                TaskItem(
                    task = task,
                    onTaskClick = {
                        // Zmieniamy status zadania
                        tasks = tasks.map {
                            if (it == task) it.copy(isDone = !it.isDone)
                            else it
                        }
                    },
                    onDeleteClick = {
                        taskToDelete = task // Ustawiamy zadanie do usunięcia
                    },
                    onMove = { from, to ->
                        moveTask(from, to)
                    },
                    index = index
                )
            }
        }
    }

    // AlertDialog do dodawania zadania
    if (isAddingTask) {
        AlertDialog(
            onDismissRequest = { isAddingTask = false },
            title = { Text("Dodaj zadanie") },
            text = {
                Column {
                    Text("Wpisz nazwę zadania:")
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.Gray.copy(alpha = 0.1f))
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        addTask() // Dodanie zadania po zatwierdzeniu
                        isAddingTask = false // Zamknięcie dialogu
                    }
                ) {
                    Text("Dodaj")
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddingTask = false }) {
                    Text("Anuluj")
                }
            }
        )
    }

    // AlertDialog do usuwania zadania
    if (taskToDelete != null) {
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = { Text("Czy na pewno chcesz usunąć to zadanie?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTask(taskToDelete!!) // Usunięcie zadania
                    }
                ) {
                    Text("Tak")
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToDelete = null }) {
                    Text("Anuluj")
                }
            }
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMove: (from: Int, to: Int) -> Unit,
    index: Int
) {
    var offsetY by remember { mutableStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        offsetY += delta
    }

    // Obramowanie i stylizacja zadania
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .draggable(
                state = draggableState,
                orientation = Orientation.Vertical,
                onDragStarted = { offsetY = 0f },
                onDragStopped = {
                    val newIndex = index + (offsetY / 50).toInt()
                    if (newIndex >= 0 && newIndex < index) {
                        onMove(index, newIndex)
                    } else if (newIndex > index && newIndex < index + 1) {
                        onMove(index, newIndex)
                    }
                    offsetY = 0f
                }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Cień dla efektu wizualnego
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox do oznaczenia zadania jako wykonane
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onTaskClick() }
            )

            // Tekst zadania
            Text(
                text = task.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )

            // Przycisk X do usunięcia zadania
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
