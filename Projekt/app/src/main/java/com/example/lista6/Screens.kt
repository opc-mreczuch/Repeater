package com.example.lista6

// No to wiadomka że deklaracja obiektu screen czy coś

sealed class Screens(val route: String) {
    data object HomeScreen : Screens("HomeScreen")
    data object WorkScreen : Screens("WorkScreen")
}