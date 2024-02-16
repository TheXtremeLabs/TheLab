package com.riders.thelab.feature.theaters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primaryContainer
import com.riders.thelab.core.ui.compose.theme.samsungSangFamily
import kotlinx.coroutines.delay


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TheatersSplash() {
    val scale = remember { Animatable(initialValue = 2f) }
    val theaterAdditionalTextVisibility = remember { mutableStateOf(false) }
    val visible = remember { mutableStateOf(false) }

    TheLabTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.scale(scale.value),
                    text = "T",
                    style = TextStyle(
                        fontFamily = samsungSangFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        color = md_theme_dark_primaryContainer
                    ),
                    maxLines = 1
                )
                AnimatedVisibility(visible = if (LocalInspectionMode.current) true else theaterAdditionalTextVisibility.value) {
                    Text(
                        modifier = Modifier.scale(scale.value),
                        text = "heaters",
                        style = TextStyle(
                            fontFamily = samsungSangFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            color = md_theme_dark_primaryContainer
                        ),
                        maxLines = 1
                    )
                }
            }

            AnimatedVisibility(visible = if (LocalInspectionMode.current) true else visible.value) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Powered By",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    /*Image(
                        modifier = Modifier.height(12.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_the),
                        contentDescription = "the_icon",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Image(
                        modifier = Modifier.height(12.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_lab),
                        contentDescription = "lab_icon",
                        colorFilter = ColorFilter.tint(Color.White)
                    )*/
                    Image(
                        modifier = Modifier.height(20.dp),
                        painter = painterResource(id = R.drawable.tmdb_logo),
                        contentDescription = "tmdb_icon",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    LaunchedEffect(scale) {
        delay(750L)
        scale.animateTo(targetValue = 1f, initialVelocity = 0.3f)
    }

    LaunchedEffect(theaterAdditionalTextVisibility) {
        delay(1000L)
        theaterAdditionalTextVisibility.value = true
    }

    LaunchedEffect(visible) {
        delay(1300L)
        visible.value = true
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewTheatersSplash() {
    TheLabTheme(darkTheme = true) {
        TheatersSplash()
    }
}