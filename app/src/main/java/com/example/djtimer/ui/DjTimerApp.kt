package com.example.djtimer.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.djtimer.ui.screen.InputScreen
import com.example.djtimer.ui.screen.TimerScreen

@Composable
fun DJTimerApp() {
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(
            navController = navController,
            startDestination = "input"
        ) {
            composable("input") {
                InputScreen { startH, startM, endH, endM ->
                    navController.navigate("timer/$startH/$startM/$endH/$endM")
                }
            }
            composable(
                route = "timer/{startH}/{startM}/{endH}/{endM}",
                arguments = listOf(
                    navArgument("startH") { type = NavType.IntType },
                    navArgument("startM") { type = NavType.IntType },
                    navArgument("endH") { type = NavType.IntType },
                    navArgument("endM") { type = NavType.IntType },
                )
            ) { backStackEntry ->
                val startH = backStackEntry.arguments?.getInt("startH") ?: 0
                val startM = backStackEntry.arguments?.getInt("startM") ?: 0
                val endH = backStackEntry.arguments?.getInt("endH") ?: 0
                val endM = backStackEntry.arguments?.getInt("endM") ?: 0

                TimerScreen(startH, startM, endH, endM, onDone = {
                    navController.popBackStack()
                })
            }
        }

    }
}