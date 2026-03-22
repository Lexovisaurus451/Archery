package com.lexovisaurus.archery

import android.content.Context
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.mutableIntListOf
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lexovisaurus.archery.ui.theme.ArcheryTheme

var score = Score()
var scoreArray = Array(6) { 0 }
var scoreTarget = 300
// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArcheryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    //val name = intPreferencesKey("Android")
    val name = remember { mutableStateOf("Android") }
    val statArray = remember { mutableIntListOf(1,1,1,1,1,1,1) }
    var numberOfArrows = remember { mutableIntStateOf(3) }
    Column {
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(stringResource(R.string.hello, name.value, "!"), Modifier.padding(24.dp))
            TextButton(onClick = {}, enabled = false) { Icon(painterResource(R.drawable.ic_settings), contentDescription = null) }
        }
        Column {
            val openAlertDialog = remember { mutableStateOf(false) }
            val currentMatch = remember { mutableStateOf(false) }
            val openAddDialog = remember { mutableStateOf(false) }
            Card(modifier = Modifier
                .size(width = 480.dp, height = 240.dp)
                .padding(16.dp)) {
                Text(text = stringResource(R.string.current_training), modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp))
                if (currentMatch.value) {
                    Column (horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total = ${score.getTotal()}", Modifier
                            .padding(16.dp)
                            .fillMaxWidth())
                        Row (verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
                            FloatingActionButton(onClick = {openAddDialog.value = true}, Modifier.padding(16.dp)) { Icon(painterResource(R.drawable.ic_add), contentDescription = null) }
                            SmallFloatingActionButton(onClick = {
                                statArray[6] += score.getTotal()
                                score.setTotal(0)
                                currentMatch.value = false
                            }, Modifier.padding(16.dp)) { Icon(painterResource(R.drawable.ic_stop), contentDescription = null) }
                        }
                    }
                }
                else {
                    Column {
                        Text(stringResource(R.string.no_current_training_message), Modifier.padding(16.dp), textAlign = TextAlign.Center)
                        FloatingActionButton(onClick = {openAlertDialog.value = true}, Modifier.align(Alignment.CenterHorizontally)) { Text("New match", Modifier.padding(16.dp)) }
                    }
                }
            }

            if (openAlertDialog.value) {
                numberOfArrows.intValue = newMatch(onDismissRequest = {
                    openAlertDialog.value = false
                }, onConfirmation = {
                    openAlertDialog.value = false
                    currentMatch.value = true
                    score.setTotal(0)
                })
            }

            when {
                openAddDialog.value->AddDialog(onDismissRequest = {
                    openAddDialog.value = false
                }, onConfirmation = {
                    openAddDialog.value = false
                    score.setScore(scoreArray[0] + scoreArray[1] + scoreArray[2] + scoreArray[3] + scoreArray[4] + scoreArray[5])
                }, numberOfArrows = numberOfArrows.intValue)
            }

            //Stat Card

            Card(modifier = Modifier
                .size(width = 480.dp, height = 240.dp)
                .padding(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("My Stats", modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp))
                    /*Row(verticalAlignment = Alignment.Bottom) {
                        val maxScore = remember { mutableIntStateOf(scoreTarget) }
                        for (i in 1..7) {
                            val size = remember { mutableIntStateOf((((statArray[i-1])*100)/maxScore.intValue)+25) }
                            Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                                Card(modifier = Modifier.defaultMinSize(((425/7)-16).dp, size.intValue.dp).padding(8.dp), colors = CardDefaults.outlinedCardColors()) {
                                    Text(text = (statArray[i-1]-1).toString(), modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 12.sp)
                                }
                                Text("J-${7-i}", fontSize = 12.sp)
                            }
                        }
                    }*/
                    var currentProgress by remember { mutableFloatStateOf((score.getTotal()/scoreTarget).toFloat()) }
                    LinearProgressIndicator(progress = {currentProgress}, modifier = Modifier.fillMaxWidth().padding(8.dp))
                    Text(stringResource(R.string.stats_shooted, 55.toString(), 55.toString(), 55.toString()), Modifier.align(Alignment.CenterHorizontally).padding(8.dp))
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun newMatch(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
): Int {
    var checkedChrono by remember { mutableStateOf(false) }
    var arrowsNumberDialog by remember { mutableFloatStateOf(3f) }
    ModalBottomSheet(onDismissRequest = { onDismissRequest() }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "New Match",
                modifier = Modifier.padding(16.dp),
            )
            Row {
                Switch(
                    checked = checkedChrono,
                    onCheckedChange = {
                        checkedChrono = it
                    },
                    enabled = false
                )

                Text(
                    text = "Chronotir",
                    modifier = Modifier.padding(16.dp)
                )

            }

            Slider(
                value = arrowsNumberDialog,
                onValueChange = { arrowsNumberDialog = it },
                enabled = true,
                steps = 4,
                valueRange = 1f..6f
            )
            Text(text = arrowsNumberDialog.toString())

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Dismiss")
                }
                TextButton(
                    onClick = { onConfirmation() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Confirm")
                }
            }
        }
    }
    return arrowsNumberDialog.toInt()
}

@Composable
fun AddDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    numberOfArrows: Int
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(240.dp, 50.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                for (i in 1..numberOfArrows) {
                    val scoreInt = remember { mutableIntStateOf(0) }
                    val tooHigh = remember { mutableStateOf(false) }
                    val tooLow = remember { mutableStateOf(false) }
                    if (scoreInt.intValue > 9) {
                        tooHigh.value = true
                        tooLow.value = false
                    }
                    else {
                        tooLow.value = false
                        tooHigh.value = false
                    }
                    if (scoreInt.intValue < 1) {
                        tooHigh.value = false
                        tooLow.value = true
                    }
                    Row (modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Arrow $i = ${scoreInt.intValue}")
                        TextButton(onClick = { scoreInt.intValue++}, enabled = !tooHigh.value) { Icon(painterResource(R.drawable.ic_add), contentDescription = null) }
                        TextButton(onClick = { scoreInt.intValue--}, enabled = !tooLow.value) { Icon(painterResource(R.drawable.ic_remove), contentDescription = null) }
                    }
                    scoreArray[i-1] = scoreInt.intValue
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

class Score() {
    //We're assuming that the "score" is the score for the 3 or 6 arrows.
    private var score = 0

    private var total = 0
    fun getScore(): Int {
        return score
    }
    fun getTotal(): Int {
        return total
    }
    fun setScore(newScore: Int) {
        score = newScore
        setTotal(score)
    }
    fun setTotal(newTotal: Int) {
        total += newTotal
    }
    /*suspend fun incrementCounter() {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[EXAMPLE_COUNTER] = (preferences[EXAMPLE_COUNTER] ?: 0) + 1
            }
        }
    }*/

}

@Composable
fun Feed() {
    Card(Modifier.fillMaxWidth()) {
        Text("Not implemented yet !")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArcheryTheme {
        Greeting()
    }
}
