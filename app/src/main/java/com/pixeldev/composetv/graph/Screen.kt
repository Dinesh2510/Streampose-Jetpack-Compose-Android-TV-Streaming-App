package com.pixeldev.composetv.graph

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object DashBoardScreen : Screen("dashboard")
    object HomeScreen : Screen("home")
    object SearchScreen : Screen("Search")
    object FavoritesScreen : Screen("Favorites")
    object SettingsScreen : Screen("Settings")
    object ProfileScreen : Screen("Profile")
    object UserDetails : Screen("userDetails")
    object CategoriesScreen : Screen("Categories")
    object MoviesScreen : Screen("Movies")
    object ShowsScreen : Screen("Shows")
    object MovieDetails : Screen("movieDetails")
}