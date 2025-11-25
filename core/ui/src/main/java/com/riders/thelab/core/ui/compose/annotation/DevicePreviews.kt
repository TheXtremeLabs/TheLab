package com.riders.thelab.core.ui.compose.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

///////////////////////////////////////
//
// MOBILE
//
///////////////////////////////////////
@Preview(
    name = "Pixel Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_3A
)
@Preview(
    name = "Pixel Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_3A
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
annotation class DevicePreviewsPhoneOnly

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
)
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
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
annotation class DevicePreviewsFoldableAndTablet

@DevicePreviewsPhoneOnly
@DevicePreviewsFoldableAndTablet
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
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
    device = Devices.TV_720p
)
@Preview(
    name = "tv 720p dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.TV_720p
)
@Preview(
    name = "tv 1080p light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.TV_1080p
)
@Preview(
    name = "tv 1080p dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.TV_1080p
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
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
annotation class DevicePreviewsTV

@DevicePreviews
@DevicePreviewsTV
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
annotation class DevicePreviewsAll