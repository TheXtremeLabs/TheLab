package com.riders.thelab.core.ui.compose.component.dynamicisland

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.utils.customBlur

@Composable
fun MetaEntity(
    modifier: Modifier = Modifier,
    blur: Float = 30f,
    metaContent: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = if (!LabCompatibilityManager.isTiramisu()) Modifier else Modifier.customBlur(
                blur
            ),
            content = metaContent
        )
        content()
    }
}