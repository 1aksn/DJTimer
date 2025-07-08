package com.example.djtimer.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DoneScreen(navController: NavController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFF1493))
       ,contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("DONE", fontSize = 120.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White)
            Button(onClick = {
                navController.navigate("input") {
                    popUpTo(0) { inclusive = true }
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF1493),  // 背景色
                    contentColor = Color.Blue       // 文字色
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.width(300.dp)) {
                Text("Reset")
            }
        }
    }
}