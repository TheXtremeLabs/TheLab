package com.riders.thelab.core.ui.compose.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.previewprovider.TextContentPreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////
//
// COMPOSABLE
//
///////////////////////////
@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun TheLabTopAppBar(
    @PreviewParameter(TextContentPreviewProvider::class) title: String,
    navigationIcon: @Composable() (() -> Unit)? = null
) {
    val context = LocalContext.current

    TheLabTheme {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black, MaterialTheme.colorScheme.background)
                    )
                ),
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = title, color = Color.White)
                }
            },
            navigationIcon = {
                IconButton(onClick = { (context as ComponentActivity).onBackPressed() }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent)
        )
    }
}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheLabTopAppBar(navigationIcon: @Composable() (() -> Unit)) {
    val context = LocalContext.current

    TheLabTheme {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(Color.Transparent),
            title = {
            },
            navigationIcon = {
                IconButton(onClick = { (context as ComponentActivity).onBackPressed() }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent)
        )
    }
}


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun TheLabTopAppBarLarge() {

    val context = LocalContext.current

    TheLabTheme {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black, Color.Transparent)
                    )
                ),
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Welcome to ", color = Color.White)
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier = Modifier.height(16.dp),
                            painter = painterResource(id = R.drawable.ic_lab_6_the),
                            contentDescription = "the_icon",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier = Modifier.height(16.dp),
                            painter = painterResource(id = R.drawable.ic_lab_6_lab),
                            contentDescription = "lab_icon",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier = Modifier.height(16.dp),
                            painter = painterResource(id = R.drawable.ic_the_lab_12_logo_white),
                            contentDescription = "lab_twelve",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { (context as ComponentActivity).onBackPressed() }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(Color.Transparent)
        )
    }
}

///////////////////////////
//
// PREVIEWS
//
///////////////////////////
@DevicePreviews
@Composable
private fun PreviewTheLabTopAppBar() {
    TheLabTheme {
        TheLabTopAppBar("Lorem Ipsum")
    }
}

@DevicePreviews
@Composable
private fun PreviewTheLabTopAppBarLarge() {
    TheLabTheme {
        TheLabTopAppBarLarge()
    }
}