package com.capstone.nutrizen.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Scan : Screen("scan")
    object History : Screen("history")
    object Profile : Screen("profile")

}
