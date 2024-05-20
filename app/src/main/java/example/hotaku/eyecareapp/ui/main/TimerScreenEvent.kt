package example.hotaku.eyecareapp.ui.main

import android.content.Context

sealed class TimerScreenEvent() {

    data class StartService(val context: Context): TimerScreenEvent()

    data class StartTimer(val context: Context): TimerScreenEvent()

    data class StopTimer(val context: Context): TimerScreenEvent()

}