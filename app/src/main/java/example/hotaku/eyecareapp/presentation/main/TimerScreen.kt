package example.hotaku.eyecareapp.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import example.hotaku.eyecareapp.R
import example.hotaku.eyecareapp.presentation.components.EyeCareTopBar
import example.hotaku.eyecareapp.ui.theme.EyeCareAppTheme
import example.hotaku.eyecareapp.presentation.utils.activity
import example.hotaku.eyecareapp.presentation.utils.openGithubPage
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerScreenViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val context = LocalContext.current
    val stateColor = if (state.isBreak) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    // request notification permissions if API level 33 or higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        LaunchedEffect(key1 = permissionState) {
            permissionState.launchPermissionRequest()
        }

    }

    DisposableEffect(key1 = true) {
        viewModel.onEvent(TimerScreenEvent.StartService(context))
        onDispose {
            viewModel.onEvent(TimerScreenEvent.UnbindService(context))
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.stopServiceChannel.collectLatest { isRun ->
            if (isRun) return@collectLatest
            context.activity()?.finish()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EyeCareTopBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { context.openGithubPage() },
                        painter = painterResource(id = R.drawable.main_screen_github),
                        contentDescription = "Info"
                    )
                }
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!permissionState.status.isGranted) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = 12.dp))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = .2f))
                        .padding(8.dp),
                    text = stringResource(R.string.timer_screen_revoked_notification_message),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(260.dp),
                    color = stateColor,
                    trackColor = Color.Gray.copy(alpha = .2f),
                    progress = { state.progress }
                )
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .offset(y = -64.dp),
                    painter = if (state.isBreak) painterResource(id = R.drawable.all_invisible) else painterResource(
                        id = R.drawable.all_visible
                    ),
                    contentDescription = null,
                    tint = stateColor
                )
                Text(
                    fontSize = 68.sp,
                    fontWeight = FontWeight.Light,
                    text = state.time
                )
                Text(
                    modifier = Modifier.offset(y = 64.dp),
                    fontSize = 24.sp,
                    color = stateColor,
                    text = when (state.isBreak) {
                        true -> stringResource(R.string.timer_screen_message_break)
                        false -> stringResource(R.string.timer_screen_message_continue)
                    }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.fillMaxWidth(.6f),
                shape = MaterialTheme.shapes.large,
                colors = ButtonColors(
                    containerColor = when (state.isTimerStarted) {
                        true -> MaterialTheme.colorScheme.error
                        false -> MaterialTheme.colorScheme.primary
                    },
                    contentColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = Color.Unspecified,
                    disabledContentColor = Color.Unspecified,
                ),
                onClick = {
                    when (state.isTimerStarted) {
                        true -> viewModel.onEvent(TimerScreenEvent.StopTimer)
                        false -> viewModel.onEvent(TimerScreenEvent.StartTimer(context))
                    }
                }
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    fontSize = 24.sp,
                    text = when (state.isTimerStarted) {
                        true -> stringResource(R.string.timer_screen_button_stop)
                        false -> stringResource(R.string.timer_screen_button_start)
                    }
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.main_screen_what_is_20_20_20)
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.main_screen_rule_description)
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.main_screen_rule_explain)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimerScreenPreviewLight() {
    EyeCareAppTheme(
        darkTheme = false
    ) {
        TimerScreen()
    }
}

@Preview
@Composable
private fun TimerScreenPreviewDark() {
    EyeCareAppTheme(
        darkTheme = true
    ) {
        TimerScreen()
    }
}