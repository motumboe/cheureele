package com.example.cheureele

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cheureele.ui.theme.CheUreEleTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheUreEleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(onRefresh = ::refreshWidgets)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshWidgets()
    }

    private fun refreshWidgets() {
        YourWidgetProvider.requestImmediateUpdate(this)
    }
}

@Composable
private fun HomeScreen(onRefresh: () -> Unit) {
    var preview by remember { mutableStateOf(WidgetPreview.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = System.currentTimeMillis()
            val delayMillis = (getNextMinuteTriggerAt(now) - now).coerceAtLeast(1L)
            delay(delayMillis)
            preview = WidgetPreview.now()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.main_title),
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            text = stringResource(R.string.main_subtitle),
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.main_preview_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = preview.numericTime,
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = preview.textTime,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }
        }

        Button(
            onClick = {
                preview = WidgetPreview.now()
                onRefresh()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.main_refresh_button))
        }

        Text(
            text = stringResource(R.string.main_refresh_hint),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = stringResource(R.string.main_usage_title),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = stringResource(R.string.main_usage_steps),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private data class WidgetPreview(
    val numericTime: String,
    val textTime: String
) {
    companion object {
        fun now(): WidgetPreview = WidgetPreview(
            numericTime = getTimeNumber(),
            textTime = getTimeText()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    CheUreEleTheme {
        HomeScreen(onRefresh = {})
    }
}
