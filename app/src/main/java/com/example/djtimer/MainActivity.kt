package com.example.djtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.djtimer.nav.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

//import com.example.djtimer.ui.DjTimerApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // DjTimerApp()
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    }
}