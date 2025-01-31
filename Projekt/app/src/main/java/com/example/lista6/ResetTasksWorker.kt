package com.example.lista6.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lista6.TaskDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar


// Klasa Odpowiedzialna za restart danych po upływie czasu ( Sprawdza daty i restartuje checkboxy)

class ResetTasksWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val taskDataStore = TaskDataStore(applicationContext)

            val currentDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val lastResetDate = taskDataStore.getLastResetDate()

            if (currentDate > lastResetDate) {
                val homeTasks = taskDataStore.getHomeTasks()
                val updatedHomeTasks = homeTasks.map { task ->
                    if (task.isDone) task.copy(isDone = false) else task
                }

                taskDataStore.saveHomeTasks(updatedHomeTasks)
                val workTasks = taskDataStore.getWorkTasks()
                val updatedWorkTasks = workTasks.map { task ->
                    if (task.isDone) task.copy(isDone = false) else task
                }

                taskDataStore.saveWorkTasks(updatedWorkTasks)
                taskDataStore.saveLastResetDate(currentDate)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}