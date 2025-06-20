package com.example.djtimer.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.djtimer.ui.DoneScreen
import com.example.djtimer.ui.InputTimeScreen
import com.example.djtimer.ui.TimerCountScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "input") {

        composable("input") {
            InputTimeScreen(navController)
        }

        composable("timer") {
            TimerCountScreen(navController)
        }

        composable("done") {
            DoneScreen(navController)
        }
    }
}