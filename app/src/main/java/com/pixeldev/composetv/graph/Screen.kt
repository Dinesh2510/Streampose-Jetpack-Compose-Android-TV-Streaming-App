package com.pixeldev.composetv.graph

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object UserDetails : Screen("userDetails")
}