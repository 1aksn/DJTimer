package com.example.djtimer

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import com.example.djtimer.nav.AppNavGraph
import com.example.djtimer.ui.theme.DJTimerTheme
import com.example.djtimer.viewModel.DJTimerViewModel
import dagger.hilt.android.AndroidEntryPoint

//import com.example.djtimer.ui.DjTimerApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: DJTimerViewModel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
            val startScreen by viewModel.currentScreen.collectAsState()

            val navController = rememberNavController()

            // ✅ 最後に見てた画面へ自動遷移
            LaunchedEffect(startScreen) {
                if (startScreen != "input") {
                    navController.navigate(startScreen) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            DJTimerTheme {
                AppNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun HideSystemBars() {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}