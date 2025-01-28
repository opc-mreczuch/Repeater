package com.example.lista6.model

data class Task(
    val name: String,
    var isDone: Boolean = false,
    var taskNumber: Int = 0 // Nowa właściwość dla numeru zadania
)
