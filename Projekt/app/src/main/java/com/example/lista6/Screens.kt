package com.example.lista6

sealed class Screens(val route: String) {
    data object HomeScreen : Screens("HomeScreen")
    data object WorkScreen : Screens("WorkScreen")
}