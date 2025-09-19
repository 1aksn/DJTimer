package com.example.djtimer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.pressBack
import org.junit.Test

/**
 * DoneScreen UI テスト
 *
 * 前提：
 * - NavGraph は startDestination = "input"
 * - DoneScreen は "input" エントリの ViewModel を参照
 * - InputTimeScreen の PlayTime 入力欄には testTag "playTimeField" が付いている（既存テストと同じ）
 */
class DoneScreenTest : ComposeRules() {

    /**
     *  RESET 押下 → アニメーション完了 → InputTime に戻る
     *
     * DoneScreen の RESET はアニメーション後に popBackStack("input", false) を行う実装なので、
     * 条件待ちで PlayTime 欄の再表示を待つ。
     */
    @Test
    fun reset押下でInputに戻る() {
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("1")
        composeRule.onNodeWithText("GO").performClick()

        // Timer画面に来たことを確認
        composeRule.onNodeWithText("Reaming time").assertIsDisplayed()

        // 63秒以内にDONE画面へ遷移するのを待つ
        composeRule.waitUntil(timeoutMillis = 63_000) {
            composeRule.onAllNodesWithText("DONE!!").fetchSemanticsNodes().isNotEmpty()
        }

        // DONE表示を確認
        composeRule.onNodeWithText("DONE!!").assertIsDisplayed()

        composeRule.onNodeWithText("RESET").performClick()

        // アニメーション終了後に InputTime に戻るのを待つ（最大3秒）
        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodes(hasTestTag("playTimeField")).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("playTimeField").assertIsDisplayed()
    }

    /**
     *  端末の戻るキーで戻れない（BackHandler 無効化）
     */
    @Test
    fun バックキーは無効() {
        composeRule.onNodeWithText("PlayTime（min）").performTextInput("1")
        composeRule.onNodeWithText("GO").performClick()

        // Timer画面に来たことを確認
        composeRule.onNodeWithText("Reaming time").assertIsDisplayed()


        // 63秒以内にDONE画面へ遷移するのを待つ
        composeRule.waitUntil(timeoutMillis = 63_000) {
            composeRule.onAllNodesWithText("DONE!!").fetchSemanticsNodes().isNotEmpty()
        }

        // DONE表示を確認
        composeRule.onNodeWithText("DONE!!").assertIsDisplayed()

        // 端末の戻るキーを送出
        pressBack()

        // 依然として Done に居ることを確認
        composeRule.onNodeWithText("DONE!!").assertIsDisplayed()

    }
}
