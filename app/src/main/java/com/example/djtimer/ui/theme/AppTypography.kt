package com.example.djtimer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.djtimer.R

// Google Fonts Providerを利用
val luckiestGuyFontFamily = FontFamily(
    Font(R.font.galindo)
)

// Typographyに適用
val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = luckiestGuyFontFamily),
    displayMedium = TextStyle(fontFamily = luckiestGuyFontFamily),
    displaySmall = TextStyle(fontFamily = luckiestGuyFontFamily),
    headlineLarge = TextStyle(fontFamily = luckiestGuyFontFamily),
    headlineMedium = TextStyle(fontFamily = luckiestGuyFontFamily),
    headlineSmall = TextStyle(fontFamily = luckiestGuyFontFamily),
    titleLarge = TextStyle(fontFamily = luckiestGuyFontFamily),
    titleMedium = TextStyle(fontFamily = luckiestGuyFontFamily),
    titleSmall = TextStyle(fontFamily = luckiestGuyFontFamily),
    bodyLarge = TextStyle(fontFamily = luckiestGuyFontFamily),
    bodyMedium = TextStyle(fontFamily = luckiestGuyFontFamily),
    bodySmall = TextStyle(fontFamily = luckiestGuyFontFamily),
    labelLarge = TextStyle(fontFamily = luckiestGuyFontFamily),
    labelMedium = TextStyle(fontFamily = luckiestGuyFontFamily),
    labelSmall = TextStyle(fontFamily = luckiestGuyFontFamily),
)
