package com.riders.thelab.feature.kat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import timber.log.Timber


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KatTopAppBar(title: String, onNavigationBackClicked: () -> Unit) {

    TheLabTheme {
        TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
//                    colors = CardDefaults.cardColors(containerColor = if (!isSystemInDarkTheme()) md_theme_dark_primaryContainer else md_theme_light_primaryContainer)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                        }
                    }

                    Text(text = title)
                }
            }, navigationIcon = {
                Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                    IconButton(onClick = onNavigationBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "navigation_back_icon"
                        )
                    }
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KatSendButton(onSendClicked: () -> Unit) {
    Card(modifier = Modifier.size(56.dp), onClick = onSendClicked, shape = CircleShape) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Filled.Send, contentDescription = "send_icon")
        }
    }
}

@Composable
fun KatContent(viewModel: KatMainViewModel) {
    val context = LocalContext.current

    val lazyListState = rememberLazyListState()
    val textInput = remember { mutableStateOf("") }

    TheLabTheme {
        Scaffold(
            topBar = {
                KatTopAppBar(title = viewModel.extraUsername) {
                    (context.findActivity() as KatActivity).backPressed()
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Bottom
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = lazyListState
                ) {

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(2f),
                        value = textInput.value,
                        onValueChange = { textInput.value = it },
                        placeholder = { Text(text = "Enter a message") },
                        shape = RoundedCornerShape(50),
                        colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.Transparent)
                    )
                    Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.Center) {
                        KatSendButton {
                            Timber.d("Send message: ${textInput.value}")
                        }
                    }
                }
            }
        }

    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewKatTopAppBar() {
    TheLabTheme {
        KatTopAppBar("JaneDoe255") {

        }
    }
}

@DevicePreviews
@Composable
private fun PreviewKatSendButton() {
    TheLabTheme {
        KatSendButton {}
    }
}

@DevicePreviews
@Composable
private fun PreviewKatContent() {
    val viewModel: KatMainViewModel = hiltViewModel()
    TheLabTheme {
        KatContent(viewModel = viewModel)
    }
}