package com.example.lista6.model // Dodaj odpowiednią paczkę

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lista6.model.Task

@Composable
fun TaskItem(task: Task, onTaskClick: () -> Unit) {
    Row(modifier = Modifier.padding(16.dp)) {
        Checkbox(
            checked = task.isDone,
            onCheckedChange = { onTaskClick() }
        )
        Text(
            text = task.name,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
