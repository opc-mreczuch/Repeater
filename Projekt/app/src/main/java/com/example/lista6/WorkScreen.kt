package com.example.lista6.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lista6.model.Task
import kotlinx.coroutines.launch
import com.example.lista6.TaskDataStore

// Funkcję strony, Dodawanie/Usuwanie i przesuwanie zadań
@Composable
fun WorkScreen(taskDataStore: TaskDataStore) {
    val tasks by taskDataStore.workTasksFlow.collectAsState(initial = emptyList())
    var taskName by remember { mutableStateOf("") }
    var isAddingTask by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun addTask() {
        if (taskName.isNotEmpty()) {
            val newTask = Task(name = taskName, taskNumber = tasks.size + 1)
            coroutineScope.launch {
                taskDataStore.saveWorkTasks(tasks + newTask)
            }
            taskName = ""
        }
    }

    fun deleteTask(task: Task) {
        coroutineScope.launch {
            taskDataStore.saveWorkTasks(tasks.filter { it != task })
        }
        taskToDelete = null
    }

    fun moveTask(from: Int, to: Int) {
        val updatedTasks = tasks.toMutableList()
        val task = updatedTasks.removeAt(from)
        updatedTasks.add(to, task)

        coroutineScope.launch {
            taskDataStore.saveWorkTasks(updatedTasks.mapIndexed { index, task ->
                task.copy(taskNumber = index + 1)
            })
        }
    }

    // Wygląd strony, Napis/Przycisk itp itd
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF243642))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "WORK",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            color = Color(0xFFE2F1E7),
            textAlign = TextAlign.Center
        )


        Button(
            onClick = { isAddingTask = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF578E7E)
            )
        ) {
            Text(
                text = "Dodaj zadanie",
                color = Color(0xFF3D3D3D)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kolumna odpowiedzialna za Listę zadań
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(tasks) { index, task ->
                TaskItem(
                    task = task,
                    tasks = tasks,
                    onTaskClick = {
                        coroutineScope.launch {
                            val updatedTasks = tasks.map {
                                if (it == task) it.copy(isDone = !it.isDone)
                                else it
                            }
                            taskDataStore.saveWorkTasks(updatedTasks)
                        }
                    },
                    onDeleteClick = {
                        taskToDelete = task
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
            title = {
                Text(
                    text = "Dodaj zadanie",
                    color = Color(0xFF3D3D3D)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Wpisz nazwę zadania:",
                        color = Color(0xFF3D3D3D)
                    )
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
                        addTask()
                        isAddingTask = false
                    }
                ) {
                    Text(
                        text = "Dodaj",
                        color = Color(0xFF3D3D3D)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddingTask = false }) {
                    Text(
                        text = "Anuluj",
                        color = Color(0xFF3D3D3D)
                    )
                }
            }
        )
    }

    // AlertDialog do usuwania zadania
    if (taskToDelete != null) {
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = {
                Text(
                    text = "Czy na pewno chcesz usunąć to zadanie?",
                    color = Color(0xFF3D3D3D)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteTask(taskToDelete!!)
                    }
                ) {
                    Text(
                        text = "Tak",
                        color = Color(0xFF3D3D3D)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToDelete = null }) {
                    Text(
                        text = "Anuluj",
                        color = Color(0xFF3D3D3D)
                    )
                }
            }
        )
    }
}