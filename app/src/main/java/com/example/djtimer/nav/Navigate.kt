package com.example.djtimer.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.djtimer.ui.DoneScreen
import com.example.djtimer.ui.InputTimeScreen
import com.example.djtimer.ui.TimerCountScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "input") {

        composable("input") {
            InputTimeScreen(navController)
        }

        composable("timer") { backStackEntry ->
            TimerCountScreen(navController, backStackEntry)
        }

        composable("done") { backStackEntry ->
            DoneScreen(navController, backStackEntry)
        }
    }
}