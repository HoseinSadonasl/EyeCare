package ir.hoseinsa.eyecareapp.utils

import android.os.CountDownTimer
import android.util.Log
import ir.hoseinsa.eyecareapp.utils.TimeUtils.toTimeFormat

object TimerUtils {

    private const val MILLIS_INTERVAL = 1000L
    private var timer: CountDownTimer? = null
    private val CONTINUE_TIMER_MILLISECONDS = 10_000L
    private val BREAK_TIMER_MILLISECODS = 5_000L

    private fun getInstance(
        millisInFuture: Long,
        onTick: (tick: Long) -> Unit,
        onFinish: () -> Unit
    ): CountDownTimer = object : CountDownTimer(millisInFuture, MILLIS_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            onTick(millisUntilFinished)
            Log.e(::TimerService.name, "onTick: ${millisUntilFinished.toTimeFormat()}")
        }

        override fun onFinish() {
            timer = null
            onFinish()
        }
    }

    fun continueTimer(
        onTick: (tick: Long) -> Unit,
        onFinish: () -> Unit
    ) {
        if (timer == null) {
            timer = getInstance(
                millisInFuture = CONTINUE_TIMER_MILLISECONDS,
                onTick =  onTick,
                onFinish = onFinish
            )
        }
        timer?.start()
    }

    fun breakTimer(
        onTick: (tick: Long) -> Unit,
        onFinish: () -> Unit
    ) {
        if (timer == null) {
            timer = getInstance(
                millisInFuture = BREAK_TIMER_MILLISECODS,
                onTick = onTick,
                onFinish = onFinish
            )
        }
        timer?.start()
    }

    fun cancelTimer() {
        timer?.cancel()
    }

}