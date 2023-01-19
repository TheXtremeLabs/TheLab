package com.riders.thelab.core.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews

@DevicePreviews
@Composable
fun Welcome() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Welcome to ")
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