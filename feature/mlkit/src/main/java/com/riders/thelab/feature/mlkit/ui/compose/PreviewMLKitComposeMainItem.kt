package com.riders.thelab.feature.mlkit.ui.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.mlkit.data.local.bean.MLKitItemBean
import com.riders.thelab.feature.mlkit.data.local.model.MLKitItem


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@Composable
fun MLKitComposeMainItem(
    theme: AppTheme,
    darkTheme: Boolean,
    item: MLKitItem,
    isDragging: Boolean,
    modifier: Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    val elevation by animateDpAsState(
        targetValue = if (isDragging) 8.dp else 4.dp,
        label = "dragging_elevation_animation"
    )

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(
            modifier = modifier,
            onClick = {
                uiEvent.invoke(
                    when (item.type) {
                        MLKitItemBean.BARCODE_SCANNING -> UiEvent.OnBarcodeScannerClicked
                        MLKitItemBean.DOCUMENT_SCANNER -> UiEvent.OnDocumentScannerClicked
                        MLKitItemBean.FACE_DETECTION -> UiEvent.OnFaceDetectionClicked
                        MLKitItemBean.TEXT_RECOGNITION -> UiEvent.OnTextRecognitionClicked
                        MLKitItemBean.DIGITAL_INK_RECOGNITION -> UiEvent.OnInkRecognitionClicked
                        MLKitItemBean.TRANSLATE -> UiEvent.OnTranslateClicked
                        else -> {
                            UiEvent.None
                        }
                    }
                )
            },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(width = this.maxWidth, height = this.maxHeight),
                        painter = painterResource(id = item.banner),
                        contentDescription = "item_banner",
                        contentScale = if (MLKitItemBean.CAMERA_TEST == item.type) ContentScale.Inside else ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        text = stringResource(id = item.title),
                        style = TextStyle(fontWeight = FontWeight.W600, fontSize = 20.sp)
                    )
                    Text(
                        text = stringResource(id = item.description),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
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
@DevicePreviews
@Composable
private fun PreviewMLKitComposeMainItem(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme, darkTheme = isSystemInDarkTheme()) {
        MLKitComposeMainItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_height) + 64.dp),
            theme = appTheme, darkTheme = isSystemInDarkTheme(),
            item = PreviewProviderMLKitItem().values.first(),
            isDragging = false
        ) {}
    }
}