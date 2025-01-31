package com.example.lista6.model

import kotlinx.serialization.Serializable

// Deklaracja obiektu
@Serializable
data class Task(
    val id: Int = 0,
    val name: String,
    val taskNumber: Int,
    val isDone: Boolean = false,
)