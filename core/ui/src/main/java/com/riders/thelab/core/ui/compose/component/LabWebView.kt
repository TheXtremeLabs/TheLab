package com.riders.thelab.core.ui.compose.component

import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.viewinterop.AndroidView
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import java.nio.charset.StandardCharsets

class CustomWebViewClient : WebViewClient() {
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null && url.startsWith("https://www.google.com")) {
            return true
        }
        return false
    }
}

class CustomWebChromeClient : WebChromeClient() {
    override fun onCloseWindow(window: WebView?) {}

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        return true
    }
}

@Composable
fun LabWebView(
    modifier: Modifier = Modifier,
    htmlRawContent: String? = null,
    url: String? = null,
    webViewClient: WebViewClient? = CustomWebViewClient(),
    webViewChromeClient: WebChromeClient? = CustomWebChromeClient()
) {
    if (null == htmlRawContent && null == url) {
        throw IllegalArgumentException("html data and url string cannot be both null. Please specify one of them")
    }

    if (null != htmlRawContent && null != url) {
        throw IllegalArgumentException("html data and url string cannot be both set. Please specify either one")
    }

    val context = LocalContext.current

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        AndroidView(
            modifier = Modifier.size(width = this.maxWidth, height = this.maxHeight),
            factory = {
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    webViewClient?.let { this.webViewClient = it }
                    webViewChromeClient?.let { this.webChromeClient = it }
                }
            }
        ) {
            if (null != htmlRawContent) it.loadDataWithBaseURL(
                null,
                htmlRawContent,
                "text/html",
                StandardCharsets.UTF_8.name(),
                null
            )
            if (null != url) it.loadUrl(url)
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewLabVewView(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        LabWebView(
            modifier = Modifier.fillMaxSize(),
            url = "https://www.google.fr"
        )
    }
}