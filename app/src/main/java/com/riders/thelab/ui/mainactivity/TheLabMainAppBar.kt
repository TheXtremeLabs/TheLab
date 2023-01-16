package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_primary

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun UserCardIcon() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {

        Card(
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp),
            onClick = {
                expanded = true
            },
            shape = CircleShape
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                imageVector = Icons.Filled.Person,
                contentDescription = "user_icon"
            )
        }

        DropdownMenu(
            modifier = Modifier
                .padding(0.dp)// margin
                .padding(8.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Refresh") },
                onClick = {
                    // Handle refresh!
                })

            Divider()

            DropdownMenuItem(
                text = { Text(text = "Settings") },
                onClick = {
                    // Handle settings!
                })

            Divider()

            DropdownMenuItem(
                text = { Text(text = "Send Feedback") },
                onClick = {
                    // Handle send feedback!
                })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarContent(viewModel: MainActivityViewModel) {

    val searchedAppRequest = remember { viewModel.searchedAppRequest }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(35.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon"
            )

            Spacer(modifier = Modifier.size(8.dp))

            TextField(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding()
                //    .background(Color.Black)
                ,
                value = searchedAppRequest.value,
                onValueChange = { newValue ->  viewModel.searchApp(newValue) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    textColor = Color.White,
                    placeholderColor = Color.LightGray,
                    disabledTextColor = Color.Transparent,
                    cursorColor = md_theme_dark_primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )

            Spacer(modifier = Modifier.size(8.dp))

            UserCardIcon()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheLabMainTopAppBar(viewModel: MainActivityViewModel) {
    TheLabTheme {
        Box(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .padding(0.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppBarContent(viewModel)
        }
        /*TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                        .padding(16.dp)

                ) {
                    AppBarContent(viewModel)
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(md_theme_light_onBackground)
        )*/
    }
}