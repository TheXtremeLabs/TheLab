package com.riders.thelab.core.ui.compose.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.riders.thelab.core.ui.R

val samsungSangFamily = FontFamily(
    Font(R.font.samsungsans_thin, FontWeight.Thin),
    Font(R.font.samsungsans_light, FontWeight.Light),
    Font(R.font.samsungsans_regular, FontWeight.Normal),
    Font(R.font.samsungsans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.samsungsans_medium, FontWeight.Medium),
    Font(R.font.samsungsans_bold, FontWeight.Bold)
)

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
    )
)