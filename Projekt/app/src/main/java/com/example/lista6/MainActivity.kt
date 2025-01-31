package com.example.lista6

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lista6.ui.theme.Lista6Theme
import java.util.Calendar
import java.util.concurrent.TimeUnit
import com.example.lista6.ui.screens.HomeScreen
import com.example.lista6.ui.screens.WorkScreen
import com.example.lista6.workers.ResetTasksWorker
import com.example.lista6.workers.NotificationWorker




// MainActivity czyli wywoływanie funkcji, skierowanie na przekaz danych i takie tam


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicjalizacja TaskDataStore
        val taskDataStore = TaskDataStore(applicationContext)

        // Zaplanuj codzienne resetowanie zadań
        scheduleDailyReset()

        // Zaplanuj codzienne powiadomienia
        scheduleDailyNotification()

        // Poproś o uprawnienia
        requestNotificationPermission()

        setContent {
            Lista6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(taskDataStore = taskDataStore) // Przekaż TaskDataStore do Navigation
                }
            }
        }
    }

    private fun scheduleDailyReset() {
        val resetWorkRequest = PeriodicWorkRequestBuilder<ResetTasksWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ResetTasksWork",
            ExistingPeriodicWorkPolicy.KEEP,
            resetWorkRequest
        )
    }


    // Dzienne notyfikacje

    private fun scheduleDailyNotification() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val delay = calendar.timeInMillis - System.currentTimeMillis()

        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NotificationWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            notificationWorkRequest
        )
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }
}


// Nawigacja i bottommenu tego typu benc

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(taskDataStore: TaskDataStore) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomMenu(navController = navController) },
        content = { NavGraph(navController = navController, taskDataStore = taskDataStore) }
    )
}

@Composable
fun NavGraph(navController: NavHostController, taskDataStore: TaskDataStore) {
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(taskDataStore = taskDataStore)
        }
        composable(route = Screens.WorkScreen.route) {
            WorkScreen(taskDataStore = taskDataStore)
        }
    }
}

@Composable
fun BottomMenu(navController: NavHostController) {
    val screens = listOf(
        BottomBar.Home, BottomBar.Work
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(text = screen.title) },
                icon = { Icon(imageVector = screen.icon, contentDescription = "icon") },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}