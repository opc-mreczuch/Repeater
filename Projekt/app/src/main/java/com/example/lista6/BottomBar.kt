package com.example.lista6

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBar(Screens.HomeScreen.route, "Home", Icons.Filled.Home)
    object Work : BottomBar(Screens.WorkScreen.route, "Work", Icons.Filled.Email)
}
