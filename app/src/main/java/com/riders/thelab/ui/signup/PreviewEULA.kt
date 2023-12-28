package com.riders.thelab.ui.signup

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHtmlText
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
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
    /*val spannedText = HtmlCompat.fromHtml(
        stringResource(id = R.string.eula_content),
        if (LabCompatibilityManager.isNougat()) HtmlCompat.FROM_HTML_MODE_COMPACT else 0
    )*/

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(verticalScroll)
            ) {
                LabHtmlText(
                    modifier = Modifier.fillMaxWidth(),
                    stringResId = com.riders.thelab.core.ui.R.string.eula_content
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