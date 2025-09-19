
package com.example.djtimer

import android.widget.TimePicker
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.djtimer.ui.InputTimeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.compose.composable
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.djtimer.viewModel.DJTimerViewModel
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import java.time.LocalTime

class InputTimerScreenTest : ComposeRules() {

    /**
     * PlayTimeを入力するとGOボタンが表示されTimer画面に遷移する
     */
    @Test
    fun playTime入力でGo押下するとTimer画面へ遷移する() {
        // PlayTime入力欄に30分と入力
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("30")

        // GOボタンが表示されてクリック可能になる
        composeRule.onNodeWithText("GO").assertIsDisplayed().performClick()

        // 遷移後、Timer画面のテキストが表示されていることを確認
        composeRule.onNodeWithText("Reaming time").assertIsDisplayed()
    }

    /**
     * Start/End時刻を入力するとPlayTime欄が無効化される
     */
    @Test
    fun startEnd入力でplayTimeが無効化される() {
        // Start を開く → 12:00 にセット → OK
        composeRule.onNodeWithTag("startButton").performClick()
        composeRule.waitForIdle()

        onView(withClassName(equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(12, 0))

        // OK（プラットフォームの positive ボタン）
        onView(withId(android.R.id.button1)) // OK（ロケール非依存）
            .inRoot(isDialog())
            .perform(click())

        // End を開く → 12:45 にセット → OK
        composeRule.onNodeWithTag("endButton").performClick()
        composeRule.waitForIdle()

        onView(withClassName(equalTo(TimePicker::class.java.name)))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(PickerActions.setTime(12, 45))

        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .perform(click())

        // → inputMode が StartEnd になり、PlayTime は disabled になる想定
        composeRule.onNodeWithTag("playTimeField").assertIsNotEnabled()
    }

    @Composable
    private fun InputTimeForTest(
        onVm: (DJTimerViewModel) -> Unit
    ) {
        val nav = rememberNavController()

        // 本番同様の NavHost を用意（"input" を startDestination に）
        androidx.navigation.compose.NavHost(navController = nav, startDestination = "input") {
            composable("input") { backStackEntry ->
                // InputTimeScreen が使うのと同じオーナー（"input" の BackStackEntry）で取得
                val parentEntry = remember(backStackEntry) { nav.getBackStackEntry("input") }
                val vm: DJTimerViewModel = hiltViewModel(parentEntry)
                LaunchedEffect(Unit) { onVm(vm) }
                InputTimeScreen(navController = nav)
            }
        }
    }

    /**
     * PlayTimeを入力するとStart/Endボタンが無効化される
     */
    @Test
    fun playTime入力でstartEndが無効化される() {
        // PlayTimeに入力
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("45")

        // Start/Endボタンが無効化されていることを確認
        composeRule.onNodeWithTag("startButton").assertIsNotEnabled()
        composeRule.onNodeWithTag("endButton").assertIsNotEnabled()
    }

    /**
     * Resetボタンで入力がクリアされる
     */
    @Test
    fun resetで入力がクリアされる() {
        // PlayTimeに入力
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("60")

        // Resetを押す
        composeRule.onNodeWithText("RESET").performClick()

        // 入力欄が空になることを確認
        composeRule.onNodeWithTag("playTimeField").assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.EditableText,
                AnnotatedString("")       // 入力値
            )
        )
    }
}

