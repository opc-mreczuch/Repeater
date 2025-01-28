package com.example.lista6.ui.screens

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lista6.model.Task
import com.example.lista6.model.TaskItem

@Composable
fun WorkScreen() {
    var taskName by remember { mutableStateOf(TextFieldValue("")) }
    var tasks by remember { mutableStateOf(listOf<Task>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Nagłówek
        Text(
            text = "WORK",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Pole do dodawania zadania
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = taskName,
                onValueChange = { taskName = it },
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.2f), shape = MaterialTheme.shapes.small)
                    .padding(8.dp)
                    .fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                if (taskName.text.isNotEmpty()) {
                    tasks = tasks + Task(taskName.text)
                    taskName = TextFieldValue("") // Resetowanie pola tekstowego
                }
            }) {
                Text("Add Task")
            }
        }

        // Lista zadań
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(tasks) { task ->
                TaskItem(task = task, onTaskClick = {
                    tasks = tasks.map {
                        if (it == task) it.copy(isDone = !it.isDone)
                        else it
                    }
                })
            }
        }
    }
}

