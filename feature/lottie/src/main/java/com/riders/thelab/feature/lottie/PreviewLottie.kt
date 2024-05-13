package com.riders.thelab.feature.lottie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@DevicePreviews
@Composable
fun LottieContent() {

    val verticalScroll = rememberScrollState()

    val list = listOf(
        // Android
        "https://assets1.lottiefiles.com/datafiles/8rza4J0CdJeJ8Pb3v9INOKI0vcekEpVccKVF2lNQ/android.json",
        // Search
        "https://assets4.lottiefiles.com/packages/lf20_83et0zjc.json",
        // Loading
        "https://assets1.lottiefiles.com/private_files/lf30_bn5winlb.json",
        "https://assets5.lottiefiles.com/private_files/lf30_qsg7wqkv.json",
        // Fluid loader
        "https://assets2.lottiefiles.com/packages/lf20_kk62um5v.json",
        // Swinging
        "https://assets6.lottiefiles.com/packages/lf20_97iupqly.json",
        // Scanning files
        "https://assets5.lottiefiles.com/private_files/lf30_jasmilgh.json",

        // Backgrounds
        // IT
        "https://assets10.lottiefiles.com/packages/lf20_k5dcqzxm.json",
        // Abstract
        "https://assets7.lottiefiles.com/packages/lf20_ym8w5cx4.json",
    )

    TheLabTheme {
        Scaffold(
            topBar = { TheLabTopAppBar(title = stringResource(R.string.activity_title_lottie)) }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(verticalScroll),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                repeat(list.size) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = dimensionResource(id = R.dimen.max_card_image_height)),
                        contentAlignment = Alignment.Center
                    ) {
                        Lottie(modifier = Modifier.fillMaxWidth(), url = list[index])
                    }
                }
            }
        }
    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////