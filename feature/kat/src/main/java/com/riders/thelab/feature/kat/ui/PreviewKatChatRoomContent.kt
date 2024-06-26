package com.riders.thelab.feature.kat.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.data.local.model.kat.KatModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

                    Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }, navigationIcon = {
                Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                    IconButton(onClick = onNavigationBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "navigation_back_icon"
                        )
                    }
                }
            })
    }
}

@Composable
fun KatSendButton(onSendClicked: () -> Unit) {
    Card(modifier = Modifier.size(56.dp), onClick = onSendClicked, shape = CircleShape) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "send_icon")
        }
    }
}

@Composable
fun KatChatRoomContent(viewModel: KatChatViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    TheLabTheme {
        Scaffold(
            topBar = {
                KatTopAppBar(title = viewModel.otherUsername) {
                    (context.findActivity() as KatChatActivity).backPressed()
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
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                Timber.d("detectTapGestures | onTap")
                                focusRequester.freeFocus()
                            })

                        },
                    state = lazyListState
                ) {
                    itemsIndexed(items = viewModel.chatMessages.reversed()) { _, item ->
                        KatItem(isValid = null != viewModel.currentUserDocument, chatItem = item)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(72.dp, 180.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {

                    TextField(
                        modifier = Modifier
                            .weight(2f)
                            .focusRequester(focusRequester),
                        value = viewModel.message,
                        onValueChange = { viewModel.updateMessageText(it) },
                        placeholder = { Text(text = "Enter a message") },
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.Center) {
                        KatSendButton {
                            Timber.d("Send message: ${viewModel.message}")

                            viewModel.sendMessage(
                                context = (context.findActivity() as KatChatActivity),
                                textInput = viewModel.message
                            )

                            scrollToBottom(scope, lazyListState, viewModel.chatMessages)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel.chatMessages) {
        scrollToBottom(scope, lazyListState, viewModel.chatMessages)
    }
}

fun scrollToBottom(scope: CoroutineScope, lazyListState: LazyListState, items: List<KatModel>) {
    if (items.isNotEmpty()) {
        scope.launch {
            delay(300L)
            lazyListState.animateScrollToItem(items.lastIndex)
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
    val viewModel: KatChatViewModel = hiltViewModel()
    TheLabTheme {
        KatChatRoomContent(viewModel = viewModel)
    }
}