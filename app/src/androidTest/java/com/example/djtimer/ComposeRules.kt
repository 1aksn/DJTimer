package com.example.djtimer

import org.junit.Rule
import androidx.compose.ui.test.junit4.createAndroidComposeRule

open class ComposeRules {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()
}