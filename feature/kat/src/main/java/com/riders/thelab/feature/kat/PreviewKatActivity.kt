package com.riders.thelab.feature.kat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun KatHeader(modelName: String, userEmail: String) {
    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Phone
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.PhoneAndroid, contentDescription = "phone_icon")
                    Text(text = modelName)
                }


                // User Email
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AlternateEmail,
                        contentDescription = "phone_icon"
                    )
                    Text(text = userEmail, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun KatContent(viewModel: KatViewModel) {

    val lazyListState = rememberLazyListState()

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                state = lazyListState
            ) {
                item { KatHeader(viewModel.modelName, viewModel.userEmail) }
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
private fun PreviewKatHeader() {
    TheLabTheme {
        KatHeader(modelName = "Zebra TC58", userEmail = "test@test.fr")
    }
}

@DevicePreviews
@Composable
private fun PreviewKatContent() {
    val viewModel = KatViewModel()
    TheLabTheme {
        KatContent(viewModel = viewModel)
    }
}