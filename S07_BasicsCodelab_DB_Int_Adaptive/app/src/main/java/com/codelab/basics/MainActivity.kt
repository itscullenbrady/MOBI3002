package com.codelab.basics

import android.os.Bundle
import androidx.compose.ui.unit.sp
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme

val EldenRingPrimary = Color(0xFF2E3B2F) // Dark, muted green
val EldenRingSecondary = Color(0xFFB8860B) // Deep, rich gold
val EldenRingText = Color(0xFFF5F5DC) // Light, off-white
val EldenRingButtonBackground = Color(0xFF8B0000) // Dark, muted red
val EldenRingButtonText = Color(0xFFF5F5DC) // Light, off-white
val EldenRingFont = FontFamily(Font(R.font.mantinia)) // Custom ELDENRING-ish font

class MainActivity : ComponentActivity() {

    private var favoriteItem: DataModel? = null
    private lateinit var db: DBClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Open the DB
        db = DBClass(this@MainActivity)
        Log.d("CodeLab_DB", "onCreate: ")

        // Fetch the favorite item from the database
        favoriteItem = db.getFavoriteItem()

        // Then the real data
        setContent {
            BasicsCodelabTheme {
                MyApp(
                    modifier = Modifier.fillMaxSize(),
                    // Get the data from the DB for display
                    names = db.findAll(),
                    favoriteItem = favoriteItem,
                    onItemSelected = { item_id ->
                        db.incrementAccessCount(item_id)
                        favoriteItem = db.getFavoriteItem()
                        Log.d("MainActivity", "onItemSelected: $item_id")
                    }
                )
            }
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<DataModel>,
    favoriteItem: DataModel?,
    onItemSelected: (Long) -> Unit
) {
    val windowInfo = rememberWindowInfo()  // get size of this screen
    var index by remember { mutableIntStateOf(-1) } // which name to display
    var showMaster: Boolean = (index == -1) // fudge to force master list first, when compact

    Surface(modifier, color = EldenRingPrimary) {
        // either one page at a time, or both side-by-side
        Log.d(
            "CodeLab_DB",
            "MyApp0: index = $index "
        )
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            if (showMaster or ((index < 0) or (index >= names.size))) {      //Always Check endpoints!
                Log.d("CodeLab_DB", "MyApp1: index = $index firstTime = $showMaster")
                showMaster = false
                ShowPageMaster(names = names,
                    favoriteItem = favoriteItem,
                    updateIndex = { index = it },
                    onItemSelected = onItemSelected
                )
            } else {
                Log.d("CodeLab_DB", "MyApp2: $index ")
                ShowPageDetails(name = names[index],  // List starts at 0, DB records start at 1
                    index = index,               // use index for prev, next screen
                    updateIndex = { index = it },
                    onItemSelected = onItemSelected
                )
            }
        } else {  // show master details side-by-side
            // force visible entry if index=-1
            if (index < 0) {
                index = 0
            }
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    ShowPageMaster(
                        names = names,
                        favoriteItem = favoriteItem,
                        updateIndex = { index = it },
                        onItemSelected = onItemSelected
                    )
                }
                Column(
                    Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    ShowPageDetails(
                        name = names[index],
                        index = index,
                        updateIndex = { index = it },
                        onItemSelected = onItemSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowPageMaster(
    modifier: Modifier = Modifier,
    names: List<DataModel>,
    favoriteItem: DataModel?,
    updateIndex: (index: Int) -> Unit,
    onItemSelected: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        favoriteItem?.let {
            item {
                ShowEachListItem(name = it, pos = -1, updateIndex, onItemSelected)
            }
        }
        itemsIndexed(items = names) { pos, name ->
            Log.d("CodeLab_DB", "Item at index $pos is $name")
            ShowEachListItem(name = name, pos, updateIndex, onItemSelected)
        }
    }
}

@Composable
private fun ShowEachListItem(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    onItemSelected: (Long) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = EldenRingSecondary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name, pos, updateIndex, onItemSelected)
        Log.d("CodeLab_DB", "Greeting: ")
    }
}

@Composable
private fun CardContent(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    onItemSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = EldenRingButtonBackground,
                    contentColor = EldenRingButtonText
                ),
                onClick = {
                    updateIndex(pos)
                    onItemSelected(name.item_id)
                    Log.d("CodeLab_DB", "Details button clicked for item $pos")
                }
            ) { Text(text = "Details ${pos}", color = EldenRingButtonText) }
            Text(
                // Just the name of this record
                text = name.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = EldenRingText,
                    fontFamily = EldenRingFont
                )
            )
            if (expanded) {
                Text(
                    text = "ID: ${name.item_id}\nName: ${name.name}\nDescription: ${name.description}\nQuantity: ${name.quantity}\nType: ${name.type}\nAccess Count: ${name.access_count}",
                    fontSize = 16.sp,
                    color = EldenRingText,
                    fontWeight = FontWeight.Normal,
                    fontFamily = EldenRingFont
                )
                Log.d("CodeLab_DB", "Expanded name = ${name.toString()} ")
            }
        }
        IconButton(onClick = { expanded = !expanded
            onItemSelected(name.item_id)}) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                },
                tint = EldenRingText
            )
        }
    }
}

@Composable
private fun ShowPageDetails(
    name: DataModel,
    updateIndex: (index: Int) -> Unit,
    onItemSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    index: Int
) {
    val windowInfo = rememberWindowInfo()  // sorta global, not good
    Column(
        modifier = modifier.fillMaxWidth(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name.toString(), color = EldenRingText, fontFamily = EldenRingFont)
        Log.d("CodeLab_DB", "ShowData: $name.toString()")

        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            Button(onClick = { updateIndex(-1) }) { Text(text = "Master", color = EldenRingButtonText) }
        }
        // need check for end of array
        Button(onClick = { updateIndex(index + 1) }) { Text(text = "Next", color = EldenRingButtonText) }
        if (index > 0) {
            Button(onClick = { updateIndex(index - 1) }) { Text(text = "Prev", color = EldenRingButtonText) }
        }
    }
}