package ir.hoseinsa.eyecareapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.hoseinsa.eyecareapp.R
import ir.hoseinsa.eyecareapp.ui.components.EyeCareTopBar
import ir.hoseinsa.eyecareapp.ui.theme.EyeCareAppTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        EyeCareTopBar(title = "20-20-20")
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(260.dp),
                    trackColor = Color.Gray.copy(alpha = .2f),
                    progress = { 0.7f }
                )
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .offset(y = -64.dp),
                    painter = painterResource(id = R.drawable.all_visible),
                    contentDescription = null
                )
                Text(
                    fontSize = 68.sp,
                    fontWeight = FontWeight.Light,
                    text = "10:32"
                )
                Text(
                    modifier = Modifier.offset(y = 64.dp),
                    fontSize = 24.sp,
                    text = "CONTINUE"
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.fillMaxWidth(.6f),
                shape = MaterialTheme.shapes.large,
                onClick = { /*TODO*/ }
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    fontSize = 24.sp,
                    text = "START"
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
private fun MainScreenPreviewLight() {
    EyeCareAppTheme(
        darkTheme = false
    ) {
        MainScreen()
    }
}

@Preview
@Composable
private fun MainScreenPreviewDark() {
    EyeCareAppTheme(
        darkTheme = true
    ) {
        MainScreen()
    }
}