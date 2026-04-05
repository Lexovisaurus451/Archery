package com.lexovisaurus.archery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lexovisaurus.archery.ui.theme.ArcheryTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArcheryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting2(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.size(96.dp))
        Card(Modifier.padding(16.dp).fillMaxWidth()) {
            Text(stringResource(R.string.about_card_title), Modifier.padding(16.dp).align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
            Text(stringResource(R.string.about_message), Modifier.padding(16.dp).align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
            FilledTonalIconButton(onClick = { uriHandler.openUri("https://github.com/Lexovisaurus451/Archery") }, Modifier.align(Alignment.CenterHorizontally).padding(16.dp)) {
                Icon(painterResource(R.drawable.github_invertocat_black), contentDescription = null, Modifier.size(64.dp))
            }

        }
        Card(Modifier.padding(16.dp).fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

            }
            Text(stringResource(R.string.settings_card_title), Modifier.padding(16.dp).align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
            var name by remember { mutableStateOf("Android") }
            OutlinedTextField(
                value = name,
                onValueChange = {name = it},
                label = {
                    Text("name")
                },
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ArcheryTheme {
        Greeting2()
    }
}