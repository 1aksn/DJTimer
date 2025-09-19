
package com.example.djtimer.viewModel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.djtimer.model.InputMode
import com.example.djtimer.model.TimerState
import com.example.djtimer.util.NotificationHelper
import io.mockk.every
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.*
import org.junit.Assert.*
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.test.advanceUntilIdle
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalCoroutinesApi::class)
class DJTimerViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val context = mockk<Context>(relaxed = true)
    private lateinit var viewModel: DJTimerViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(NotificationHelper)
        every { NotificationHelper.scheduleNotification(any(), any()) } returns Unit
        viewModel = DJTimerViewModel(context)
    }

    @Before
    fun suppressAndroidLog() {
        mockkStatic(android.util.Log::class)
        every { android.util.Log.v(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 700, 0, "abc" は不正な入力としてバリデーションエラーになるか確認
    @Test
    fun `validateInput returns error for invalid playtime`() {
        viewModel.updatePlayTime("700")
        assertEquals("Input time from 1 to 600 min", viewModel.validateInput())

        viewModel.updatePlayTime("0")
        assertEquals("Input time from 1 to 600 min", viewModel.validateInput())

        viewModel.updatePlayTime("abc")
        assertEquals("Input the correct time", viewModel.validateInput())
    }

    // 正常なplayTime(10)のときnullが返るか確認
    @Test
    fun `validateInput returns null for valid playtime`() {
        viewModel.updatePlayTime("10")
        assertNull(viewModel.validateInput())
    }

    // playTimeでstartTimer()後にInProgressになり、カウントが進むか確認
//    @Test
//    fun `startTimer with playTime sets InProgress and counts down`() = runTest {
//        viewModel.updatePlayTime("1")
//        viewModel.startTimer()
//        advanceTimeBy(1000)
//        runCurrent()
//        assertEquals(TimerState.InProgress, viewModel.timerState.value)
//        advanceTimeBy(59000)
//        runCurrent()
//        assertEquals(TimerState.Done, viewModel.timerState.value)
//    }

    // stopTimer()でPausedに遷移し、残り時間が保持されているか確認
    @Test
    fun `stopTimer sets Paused and retains remaining time`() = runTest {
        viewModel.updatePlayTime("1")
        viewModel.startTimer()
        advanceTimeBy(3000)
        viewModel.stopTimer()

        assertEquals(TimerState.Paused, viewModel.timerState.value)
        assertNotNull(viewModel.remainingDurationSeconds)
    }

    // 一時停止からresumeTimerIfNeeded()で再開できるか確認
    @Test
    fun `resumeTimerIfNeeded continues countdown if not expired`() = runTest {
        viewModel.updatePlayTime("1")
        viewModel.startTimer()
        advanceTimeBy(1000)
        viewModel.stopTimer()
        viewModel.resumeTimerIfNeeded()
        advanceTimeBy(1000)
        assertEquals(TimerState.InProgress, viewModel.timerState.value)
    }

    // reset()でViewModelのすべての状態が初期化されるか確認
    @Test
    fun `reset clears all state`() {
        viewModel.updatePlayTime("10")
        viewModel.setCurrentScreen("timer")
        viewModel.reset()

        assertEquals("", viewModel.playTime.value)
        assertEquals("input", viewModel.currentScreen.value)
        assertEquals(InputMode.None, viewModel.inputMode.value)
        assertEquals(TimerState.BeforeStart, viewModel.timerState.value)
    }


    // PlayTime または Start-End が正しく入力されていれば canGo == true になるか確認
    @Test
    fun `canGo becomes true for valid inputs`() = runTest {
        viewModel.updatePlayTime("10")
        advanceUntilIdle()
        assertTrue(viewModel.canGo.first())

        viewModel.reset()
        viewModel.updateStartTime(LocalTime.of(10, 0))
        viewModel.updateEndTime(LocalTime.of(11, 0))
        assertTrue(viewModel.canGo.first())
    }


    // 入力が不完全な場合に canGo == false になるか確認
    @Test
    fun `canGo remains false for incomplete inputs`() = runTest {
        viewModel.updateStartTime(LocalTime.of(10, 0))
        assertFalse(viewModel.canGo.first())
    }


    // Start/End を設定すると inputMode が StartEnd になり playTime がクリアされるか確認
    @Test
    fun `startEnd mode sets inputMode correctly and clears playTime`() {
        viewModel.updateStartTime(LocalTime.of(10, 0))
        viewModel.updateEndTime(LocalTime.of(11, 0))

        assertEquals(InputMode.StartEnd, viewModel.inputMode.value)
        assertEquals("", viewModel.playTime.value)
        assertEquals(LocalTime.of(10, 0), viewModel.startTime.value)
        assertEquals(LocalTime.of(11, 0), viewModel.endTime.value)
    }


    // Start-End の時間差が10時間を超える場合にエラーになるか確認
    @Test
    fun `startEnd validateInput returns error if duration exceeds 10 hours`() {
        val now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)
        viewModel.updateStartTime(now)
        viewModel.updateEndTime(now.plusHours(11)) // 11時間後

        val result = viewModel.validateInput()
        assertEquals("Start to end (current time to end) must be within 10 hours", result)
    }


    // Start-End が適切な時間差のときに null が返るか確認
    @Test
    fun `startEnd validateInput returns null for valid time range`() {
        val now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)
        viewModel.updateStartTime(now)
        viewModel.updateEndTime(now.plusHours(2)) // 2時間後

        val result = viewModel.validateInput()
        assertNull(result)
    }


    // Start-End が同時刻のときに duration 0 でエラーになるか確認
    @Test
    fun `startEnd same time means zero duration and invalid`() {
        val now = LocalTime.of(9, 0)
        viewModel.updateStartTime(now)
        viewModel.updateEndTime(now) // 同じ時刻

        val result = viewModel.validateInput()
        assertEquals("Start to end (current time to end) must be within 10 hours", result)
    }

}
