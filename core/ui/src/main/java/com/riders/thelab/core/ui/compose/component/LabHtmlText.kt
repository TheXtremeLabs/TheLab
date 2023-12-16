package com.riders.thelab.core.ui.compose.component

import android.text.util.Linkify
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.method.LinkMovementMethodCompat
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun LabHtmlText(modifier: Modifier, @StringRes stringResId: Int) {
    val context = LocalContext.current
    val textColor = ContextCompat.getColor(
        context,
        if (!isSystemInDarkTheme()) R.color.black else R.color.white
    )

    val linksTextColor = ContextCompat.getColor(
        context,
        if (!isSystemInDarkTheme()) R.color.blue_grey_500 else R.color.tabColorAccent
    )

    // parsing html string using the HtmlCompat class
    val spannedText = HtmlCompat.fromHtml(
        stringResource(id = stringResId),
        if (LabCompatibilityManager.isNougat()) HtmlCompat.FROM_HTML_MODE_COMPACT else 0
    )

    Box(modifier = modifier, contentAlignment = Alignment.TopStart) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                MaterialTextView(it).apply {
                    text = spannedText
                    setTextColor(textColor)

                    // links
                    autoLinkMask = Linkify.WEB_URLS
                    linksClickable = true
                    movementMethod = LinkMovementMethodCompat.getInstance()
                    // setting the color to use forr highlihting the links
                    setLinkTextColor(linksTextColor)
                }
            },
            update = {
                // it.maxLines = currentMaxLines
                it.setTextColor(textColor)
                it.text = spannedText
            }
        )
    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewLabHtmlText() {
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            contentAlignment = Alignment.TopStart
        ) {
            LabHtmlText(
                modifier = Modifier.fillMaxSize(),
                stringResId = R.string.lorem_ipsum
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewLabHtmlTextEULA() {
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            contentAlignment = Alignment.TopStart
        ) {
            LabHtmlText(
                modifier = Modifier.fillMaxSize(),
                stringResId = R.string.eula_content
            )
        }
    }
}