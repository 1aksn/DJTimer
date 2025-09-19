package com.example.djtimer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Test

/**
 * TimerCountScreen の動作検証
 *
 * 前提：
 * - InputTimeScreen → TimerCountScreen → DoneScreen の NavGraph が起動時に構築されている
 * - Input で PlayTime を入れて GO を押すと Timer に遷移する
 * - Timer 画面には「Stop/Play（トグル）」「RESET」ボタンがある
 * - 残り時間テキストは "Reaming time"（既存テストに合わせて綴りも同一）で表示
 * - InputTimeScreen の PlayTime 入力欄はテストタグ "playTimeField" が付与されている
 */
class TimerCountScreenTest : ComposeRules() {

    /**
     * PlayTime を入力 → GO 押下 → Timer 画面に遷移して
     * Stop/Play ボタンがトグル動作することを確認
     */
    @Test
    fun タイマー画面でStopStartがトグルする() {
        // 1) InputTime で PlayTime を入力して GO
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("10")
        composeRule.onNodeWithText("GO").assertIsDisplayed().performClick()

        // 2) Timer 画面に遷移したことを確認（残り時間の見出し）
        composeRule.onNodeWithText("Reaming time").assertIsDisplayed()

        // 3) Stop → Play にトグル
        composeRule.onNodeWithText("STOP").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("START").assertIsDisplayed()

        // 4) Play → Stop に再トグル
        composeRule.onNodeWithText("START").performClick()
        composeRule.onNodeWithText("STOP").assertIsDisplayed()
    }

    /**
     * Timer 画面の RESET 押下で InputTime に戻ることを確認
     */
    @Test
    fun タイマー画面でRESET押下でInputに戻る() {
        // 1) InputTime で PlayTime を入力して GO
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("30")
        composeRule.onNodeWithText("GO").performClick()

        // 2) Timer 画面表示を確認
        composeRule.onNodeWithText("Reaming time").assertIsDisplayed()

        // 3) RESET 押下
        composeRule.onNodeWithText("RESET").performClick()

        composeRule.waitUntil(timeoutMillis = 1000) {
            composeRule.onAllNodesWithTag("playTimeField").fetchSemanticsNodes().isNotEmpty()
        }

        // 4) InputTime に戻ったこと（PlayTime 入力欄が見えること）を確認
        composeRule.onNodeWithTag("playTimeField").assertIsDisplayed()
    }

    @Test
    fun カウントが0になったらDone画面に遷移する() {
        // 1分のPlayTimeを入力してGO
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("1")
        composeRule.onNodeWithText("GO").performClick()

        // Timer画面に遷移したことを確認
        composeRule.onNodeWithText("Reaming time").assertIsDisplayed()

        // 最大65秒待機してDONE画面が表示されるのを待つ
        composeRule.waitUntil(timeoutMillis = 65_000) {
            composeRule.onAllNodesWithText("DONE!!").fetchSemanticsNodes().isNotEmpty()
        }

        // DONE画面のテキストが表示されていることを確認
        composeRule.onNodeWithText("DONE!!").assertIsDisplayed()
    }
}
