package com.riders.thelab.ui.mainactivity

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_light_onPrimaryContainer
import com.riders.thelab.core.compose.ui.theme.md_theme_light_primaryContainer
import com.riders.thelab.core.compose.utils.findActivity
import com.riders.thelab.ui.webview.WebViewActivity
import com.riders.thelab.utils.Constants


@DevicePreviews
@Composable
fun BottomSheetContent() {

    val context = LocalContext.current

    TheLabTheme {
        // Sheet content
        Box(
            modifier = Modifier
                .background(if (!isSystemInDarkTheme()) md_theme_light_primaryContainer else md_theme_light_onPrimaryContainer)
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.max_card_image_height)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.height(16.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_the),
                        contentDescription = "the_icon",
                        colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.Black else Color.White)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Image(
                        modifier = Modifier.height(16.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_lab),
                        contentDescription = "lab_icon",
                        colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.Black else Color.White)
                    )
                }


                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    "App created by Michael",
                    color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    "Find this project on Github. Link :",
                    color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W300
                )

                Button(
                    onClick = {
                        (context.findActivity() as MainActivity).startActivity(
                            Intent(
                                context,
                                WebViewActivity::class.java
                            ).apply {
                                this.putExtra(
                                    Constants.WEB_URL,
                                    "https://www.github.com/TheXtremeLabs"
                                )
                            })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {

                    Text(
                        "https://www.github.com/TheXtremeLabs",
                        color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxHeight()
                    .offset(x = 24.dp, y = 64.dp)
                    .alpha(0.4f),
                painter = painterResource(id = if (!isSystemInDarkTheme()) R.drawable.ic_the_lab_12_logo_black else R.drawable.ic_the_lab_12_logo_white),
                contentDescription = "the_lab_icon"
            )
        }
    }
}