package com.example.djtimer.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.djtimer.HideSystemBars
import com.example.djtimer.R
import com.example.djtimer.viewModel.DJTimerViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DoneScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry("input")
    }
    val viewModel: DJTimerViewModel = hiltViewModel(parentEntry)
    HideSystemBars()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFF1493))
       ,contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(id = R.string.done), fontSize = 100.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White)
            Button(onClick = {
                viewModel.reset()
                navController.popBackStack("input",false)
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700),  // 背景色
                    contentColor = Color.Blue       // 文字色
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.width(300.dp)) {
                Text(stringResource(id = R.string.reset),
                    fontSize = 20.sp)
            }
        }
    }
}