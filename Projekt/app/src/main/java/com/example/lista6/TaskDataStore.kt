package com.example.lista6

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lista6.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


// Ugułem cały mechanizm zapisywania danych całe te

// Rozszerzenie do tworzenia DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tasks_store")

class TaskDataStore(private val context: Context) {

    companion object {
        private val HOME_TASKS_KEY = stringPreferencesKey("home_tasks")
        private val WORK_TASKS_KEY = stringPreferencesKey("work_tasks")
        private val LAST_RESET_DATE_KEY = longPreferencesKey("last_reset_date")
    }

    // Odczytaj listę zadań jako Flow
    val homeTasksFlow: Flow<List<Task>> = context.dataStore.data
        .map { preferences ->
            val tasksJson = preferences[HOME_TASKS_KEY] ?: "[]"
            Json.decodeFromString(tasksJson)
        }

    val workTasksFlow: Flow<List<Task>> = context.dataStore.data
        .map { preferences ->
            val tasksJson = preferences[WORK_TASKS_KEY] ?: "[]"
            Json.decodeFromString(tasksJson)
        }


    fun getHomeTasks(): List<Task> = runBlocking {
        val tasksJson = context.dataStore.data
            .map { preferences -> preferences[HOME_TASKS_KEY] ?: "[]" }
            .first()
        Json.decodeFromString(tasksJson)
    }


    fun getWorkTasks(): List<Task> = runBlocking {
        val tasksJson = context.dataStore.data
            .map { preferences -> preferences[WORK_TASKS_KEY] ?: "[]" }
            .first()
        Json.decodeFromString(tasksJson)
    }

    // Zapisz listę zadań dla HomeScreen
    suspend fun saveHomeTasks(tasks: List<Task>) {
        context.dataStore.edit { preferences ->
            val tasksJson = Json.encodeToString(tasks)
            preferences[HOME_TASKS_KEY] = tasksJson
        }
    }

    // Zapisz listę zadań dla WorkScreen
    suspend fun saveWorkTasks(tasks: List<Task>) {
        context.dataStore.edit { preferences ->
            val tasksJson = Json.encodeToString(tasks)
            preferences[WORK_TASKS_KEY] = tasksJson
        }
    }

    // Pobierz datę ostatniego resetu
    suspend fun getLastResetDate(): Long {
        return context.dataStore.data
            .map { preferences -> preferences[LAST_RESET_DATE_KEY] ?: 0L }
            .first()
    }

    // Zapisz datę ostatniego resetu
    suspend fun saveLastResetDate(date: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_RESET_DATE_KEY] = date
        }
    }
}