package com.riders.thelab.feature.mlkit.ui.compose.barcodescanner

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.bottomsheet.BottomSheetContent
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.mlkit.data.local.model.BarcodeField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScannerResultContent(
    theme: AppTheme,
    darkTheme: Boolean,
    scanResult: BarcodeField
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (scanResult.value.isNotEmpty()) {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    sheetState = bottomSheetScaffoldState.bottomSheetState,
                    onDismissRequest = {
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.hide()
                            (context.findActivity() as BarcodeScannerResultActivity).backPressed()
                        }
                    }
                ) {
                    BottomSheetContent(theme = theme, darkTheme = darkTheme) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.QrCode2,
                                contentDescription = null
                            )
                            Text(
                                text = scanResult.type,
                                textAlign = TextAlign.Center
                            )
                        }

                        Text(
                            text = "Result found ${scanResult.value}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Box(modifier = Modifier.height(24.dp))
                    }
                }
            }
        },
        sheetPeekHeight = 128.dp,
        containerColor = Color.Transparent
    ) {}
}

@DevicePreviews
@Composable
private fun PreviewBarcodeScannerResultContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        BarcodeScannerResultContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            scanResult = BarcodeField("QR code", "FR545645161431351XD")
        )
    }
}