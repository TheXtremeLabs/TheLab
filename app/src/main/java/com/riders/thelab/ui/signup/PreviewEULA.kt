package com.riders.thelab.ui.signup

import android.text.util.Linkify
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.method.LinkMovementMethodCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun EULAScreen(viewModel: SignUpViewModel, onNavigateToUserFormScreen: () -> Unit) {

    val context = LocalContext.current
    val verticalScroll: ScrollState = rememberScrollState()
    val eulaAgreementChecked = remember { mutableStateOf(false) }
    // parsing html string using the HtmlCompat class
    val spannedText = HtmlCompat.fromHtml(
        stringResource(id = R.string.eula_content),
        if (LabCompatibilityManager.isNougat()) HtmlCompat.FROM_HTML_MODE_COMPACT else 0
    )

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(verticalScroll)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = {
                        MaterialTextView(it).apply {
                            // links
                            autoLinkMask = Linkify.WEB_URLS
                            linksClickable = true
                            movementMethod = LinkMovementMethodCompat.getInstance()
                            // setting the color to use forr highlihting the links
                            setLinkTextColor(
                                ContextCompat.getColor(
                                    context,
                                    if (!viewModel.isDarkMode) R.color.blue_grey_700 else R.color.tabColorAccent
                                )
                            )
                            setTextColor(
                                if (!viewModel.isDarkMode) ContextCompat.getColor(
                                    context,
                                    R.color.black
                                ) else ContextCompat.getColor(context, R.color.white)
                            )
                            text = spannedText
                        }
                    },
                    update = {
                        // it.maxLines = currentMaxLines
                        it.text = spannedText
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.5f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = eulaAgreementChecked.value,
                    onCheckedChange = { eulaAgreementChecked.value = it })
                Text(
                    modifier = Modifier
                        .clickable { eulaAgreementChecked.value = !eulaAgreementChecked.value },
                    text = stringResource(id = R.string.accept_agreement_license),
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        if (!eulaAgreementChecked.value) {
                            UIManager.showToast(
                                context,
                                R.string.err_msg_license_agreement_approval_mandatory
                            )
                        } else {
                            onNavigateToUserFormScreen()
                        }
                    },
                    enabled = eulaAgreementChecked.value
                ) {
                    Text(text = stringResource(id = R.string.action_continue))
                }
            }
        }
    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewEULAScreen() {
    val viewModel: SignUpViewModel = hiltViewModel()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        EULAScreen(viewModel = viewModel) {}
    }
}