package com.example.lista6.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lista6.TaskDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// Klasa odpowiedzalna za wysyłanie powiadomień

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Utwórz instancję TaskDataStore
            val taskDataStore = TaskDataStore(applicationContext)

            // Pobierz listy zadań
            val homeTasks = taskDataStore.getHomeTasks()
            val workTasks = taskDataStore.getWorkTasks()

            // Zlicz nieoznaczone zadania
            val homeUncompletedTasks = homeTasks.count { !it.isDone }
            val workUncompletedTasks = workTasks.count { !it.isDone }

            // Przygotuj wiadomość powiadomienia
            val notificationMessage = "Niewykonane zadania: Dom $homeUncompletedTasks, Praca $workUncompletedTasks"

            // Wyślij powiadomienie
            sendNotification(notificationMessage)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun sendNotification(message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Utwórz kanał powiadomień (wymagane od Androida 8.0) dlaczego to nie wiem ale tak kazali
        val channelId = "task_notifications"
        val channel = NotificationChannel(
            channelId,
            "Task Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Utwórz powiadomienie
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Niewykonane zadania")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Użyj odpowiedniej ikony
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Wyślij powiadomienie
        notificationManager.notify(1, notification)
    }
}