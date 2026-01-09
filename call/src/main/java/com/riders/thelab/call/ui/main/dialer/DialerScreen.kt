package com.riders.thelab.call.ui.main.dialer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riders.thelab.call.BuildConfig
import com.riders.thelab.call.core.utils.Constants
import com.riders.thelab.call.core.utils.LabCallManager
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@SuppressLint("MissingPermission")
@Composable
fun DialerScreen() {
    var phoneNumber by remember { mutableStateOf(if (!BuildConfig.DEBUG) "" else "0610489636") }

    val context = LocalContext.current

    val searchResults by remember(phoneNumber) {
        derivedStateOf {
            T9Search.search(phoneNumber, Constants.dummyContacts)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(searchResults) { contact ->
                    Text(
                        text = contact,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(16.dp)
            ) {
                items(items = Constants.dialPadButtons) { (number, letters) ->
                    Button(
                        onClick = {
                            if (number == "BACKSPACE") {
                                if (phoneNumber.isNotEmpty()) {
                                    phoneNumber = phoneNumber.dropLast(1)
                                }
                            } else {
                                phoneNumber += number
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .aspectRatio(1f)
                    ) {
                        if (number == "BACKSPACE") {
                            Icon(
                                imageVector = Icons.Filled.Backspace,
                                contentDescription = "Backspace"
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = number)
                                if (letters.isNotEmpty()) {
                                    Text(text = letters)
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    LabCallManager.startCall(context, phoneNumber)
//                    context.startActivity(Intent(context.findActivity() as MainActivity, CallActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Call")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDialerScreen() {
    TheLabTheme(theme = AppTheme.Default) {
        DialerScreen()
    }
}