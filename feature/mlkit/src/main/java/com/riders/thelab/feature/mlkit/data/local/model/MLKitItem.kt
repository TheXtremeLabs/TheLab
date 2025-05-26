package com.riders.thelab.feature.mlkit.data.local.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.feature.mlkit.R
import com.riders.thelab.feature.mlkit.data.local.bean.MLKitItemBean


@Stable
@Immutable
data class MLKitItem(
    val type: MLKitItemBean,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val banner: Int
) {
    companion object {

        val mock = MLKitItem(
            type = MLKitItemBean.TRANSLATE,
            title = R.string.ml_kit_item_translate_title,
            description = R.string.ml_kit_item_translate_description,
            banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_translation
        )

        var mockList = mutableListOf(
            MLKitItem(
                type = MLKitItemBean.CAMERA_TEST,
                title = R.string.ml_kit_app_name,
                description = R.string.ml_kit_app_name,
                banner = com.riders.thelab.core.ui.R.drawable.ic_google_lens_icon
            ),
            MLKitItem(
                type = MLKitItemBean.BARCODE_SCANNING,
                title = R.string.ml_kit_item_barcode_scanner_title,
                description = R.string.ml_kit_item_barcode_scanner_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_barcode_scanning
            ),
            MLKitItem(
                type = MLKitItemBean.TEXT_RECOGNITION,
                title = R.string.ml_kit_item_text_recognition_title,
                description = R.string.ml_kit_item_text_recognition_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_text_recognition
            ),
            MLKitItem(
                type = MLKitItemBean.IMAGE_LABELING,
                title = R.string.ml_kit_item_image_labeling_title,
                description = R.string.ml_kit_item_image_labeling_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_image_labeling
            ),
            MLKitItem(
                type = MLKitItemBean.FACE_DETECTION,
                title = R.string.ml_kit_item_face_detection_title,
                description = R.string.ml_kit_item_face_detection_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_face_detection
            ),
            MLKitItem(
                type = MLKitItemBean.FACE_MESH_DETECTION,
                title = R.string.ml_kit_item_face_mesh_detection_title,
                description = R.string.ml_kit_item_face_mesh_detection_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_face_mesh_detection
            ),
            MLKitItem(
                type = MLKitItemBean.TRANSLATE,
                title = R.string.ml_kit_item_translate_title,
                description = R.string.ml_kit_item_translate_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_translation
            ),
            MLKitItem(
                type = MLKitItemBean.DOCUMENT_SCANNER,
                title = R.string.ml_kit_item_document_scanner_title,
                description = R.string.ml_kit_item_document_scanner_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_document_scanner
            ),
            MLKitItem(
                type = MLKitItemBean.POSE_DETECTION,
                title = R.string.ml_kit_item_pose_detection_title,
                description = R.string.ml_kit_item_pose_detection_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_pose_detection
            ),
            MLKitItem(
                type = MLKitItemBean.SELFIE_SEGMENTATION,
                title = R.string.ml_kit_item_selfie_segmentation_title,
                description = R.string.ml_kit_item_selfie_segmentation_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_selfie_segmentation
            ),
            MLKitItem(
                type = MLKitItemBean.OBJECT_DETECTION,
                title = R.string.ml_kit_item_object_detection_title,
                description = R.string.ml_kit_item_object_detection_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_object_detection
            ),
            MLKitItem(
                type = MLKitItemBean.DIGITAL_INK_RECOGNITION,
                title = R.string.ml_kit_item_digital_ink_detection_title,
                description = R.string.ml_kit_item_digital_ink_detection_description,
                banner = com.riders.thelab.core.ui.R.drawable.ic_banner_google_ml_kit_digital_ink_recognition
            )
        )
    }
}
