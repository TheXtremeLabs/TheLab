package com.riders.thelab.core.ui.compose.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

///////////////////////////////////////
//
// MOBILE
//
///////////////////////////////////////
/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Suppress("PreviewPickerAnnotation")
@Preview(
    name = "phone light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480"
)
@Preview(
    name = "phone dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480"
)
@Preview(
    name = "landscape light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480"
)
@Preview(
    name = "landscape dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480"
)
/*
@Preview(
    name = "foldable light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480"
)
@Preview(
    name = "foldable dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480"
)*/
@Preview(
    name = "tablet light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480"
)
@Preview(
    name = "tablet dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480"
)
annotation class DevicePreviews


///////////////////////////////////////
//
// TV
//
///////////////////////////////////////
/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Suppress("PreviewPickerAnnotation")
@Preview(
    name = "tv 720p light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:parent=tv_720p,width=1280,height=720,unit=dp"
)
@Preview(
    name = "tv 720p dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=tv_720p,width=1280,height=720,unit=dp"
)
@Preview(
    name = "tv 1080p light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:parent=tv_1080p,width=1920,height=1080,unit=dp"
)
@Preview(
    name = "tv 1080p dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=tv_1080p,width=1920,height=1080,unit=dp"
)
@Preview(
    name = "tv 4K light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:parent=tv_4k,width=3480,height=2160,unit=dp"
)
@Preview(
    name = "tv 4K dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=tv_4k,width=3480,height=2160,unit=dp"
)
annotation class DevicePreviewsTV