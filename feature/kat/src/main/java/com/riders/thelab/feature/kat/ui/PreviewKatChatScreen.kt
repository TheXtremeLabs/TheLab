package com.riders.thelab.feature.kat.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.riders.thelab.core.data.local.model.kat.KatUserModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.kat.utils.FirebaseUtils


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun KatChatScreen(users: List<KatUserModel>) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    TheLabTheme {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = users.isNotEmpty(),
            label = "users_animated_content",
        ) { targetState ->
            if (!targetState) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(items = users) { _, item ->
                        ChatRoomItem(
                            username = if (item.userId == FirebaseUtils.getCurrentUserID()) "${item.username} (Me)" else item.username,
                            phoneNumber = item.phone
                        ) {
                            (context.findActivity() as KatMainActivity).launchKatChatActivity(
                                item.userId!!,
                                item.phone,
                                item.username
                            )
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
private fun PreviewKaChatScreen() {
    val list = listOf(
        KatUserModel(
            FirebaseUtils.getCurrentUserID(),
            "0614589309",
            "jahef",
            Timestamp(121251530L, 25),
            ""
        ),
        KatUserModel(
            FirebaseUtils.getCurrentUserID(),
            "0614589309",
            "JohnSmith21",
            Timestamp(121256513L, 25), ""
        ),
        KatUserModel(
            FirebaseUtils.getCurrentUserID(),
            "0614589309",
            "Mikedjcbihyd",
            Timestamp(121258953L, 25), ""
        )
    )

    TheLabTheme {
        KatChatScreen(list)
    }
}