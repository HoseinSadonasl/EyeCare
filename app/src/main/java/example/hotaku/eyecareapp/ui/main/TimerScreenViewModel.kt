package example.hotaku.eyecareapp.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import example.hotaku.eyecareapp.utils.millisToProgressValue
import example.hotaku.timer.service.TimerService
import example.hotaku.timer.utils.TimeUtils.toTimeFormat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerScreenViewModel @Inject constructor(): ViewModel() {

    private lateinit var serviceIntent: Intent
    private var service: TimerService.LocalBinder? = null
    private lateinit var timerValue: SharedFlow<Pair<Long?, Boolean>>

    var state by mutableStateOf(TimerScreenState())
        private set

    private var _stopServiceChannel = Channel<Boolean>()
    val stopServiceChannel = _stopServiceChannel.receiveAsFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            this@TimerScreenViewModel.service = service as TimerService.LocalBinder
            timerValue  = service.getService().timeer
            consumeChannelData()
            collectTimeData()
        }

        override fun onServiceDisconnected(name: ComponentName?) {}

    }

    private fun consumeChannelData() {
        viewModelScope.launch {
            service?.getService()?.isRun?.collect { isRun ->
                _stopServiceChannel.send(isRun)
                state = TimerScreenState()
            }
        }
    }

    fun onEvent(event: TimerScreenEvent) {
        when(event) {
            is TimerScreenEvent.StartService -> startService(event.context)
            is TimerScreenEvent.StartTimer -> startTimer(event.context)
            is TimerScreenEvent.StopTimer -> stopTimer()
            is TimerScreenEvent.UnbindService -> unBindService(event.context)
        }
    }

    private fun startService(context: Context) {
        serviceIntent = Intent(context, TimerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else context.startService(serviceIntent)
        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun collectTimeData() {
        viewModelScope.launch {
            timerValue.collect { timerValue ->
                if (timerValue.first == null) {
                    state = TimerScreenState()
                } else {
                    val value = millisToProgressValue(timerValue)
                    state = state.copy(
                        isTimerStarted = true,
                        isBreak = timerValue.second,
                        progress = 1 - value,
                        time = timerValue.first!!.toTimeFormat()
                    )
                }
            }
        }
    }

    private fun startTimer(context: Context) {
        startService(context)
        service?.getService()?.startTimer()
    }

    private fun stopTimer() {
        service?.getService()?.stopTimer()
        state = TimerScreenState()
    }

    private fun unBindService(context: Context) = context.unbindService(serviceConnection)

}
