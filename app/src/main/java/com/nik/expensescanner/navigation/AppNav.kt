package com.nik.expensescanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.nik.expensescanner.ui.screens.*

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("permission") { PermissionScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("camera") { CameraScreen(navController) }
        composable(
            route = "result?text={text}",
            arguments = listOf(navArgument("text") { 
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val text = backStackEntry.arguments?.getString("text") ?: ""
            ResultScreen(navController, text)
        }
        composable("dashboard") { DashboardScreen(navController) }
        composable("history") { HistoryScreen(navController) }
    }
}
