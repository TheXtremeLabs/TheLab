package com.riders.thelab.feature.download

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.Download

class PreviewProvider : PreviewParameterProvider<Download> {
    override val values: Sequence<Download>
        get() = sequenceOf(
            Download(1, "BASE_ADR_NEY_20231106_121036058.zip", 45, false, null),
            Download(4, "test_4.zip", 100, true, null),
            Download(
                5,
                "BASE_ADR_NEY_20231106_121036058.zip",
                3,
                true,
                true to "Failed to download item"
            ),
            Download(7, "test_98.zip", 75, false, null),
            Download(5, "BASE_ADR_NEY_20231106_121036058.zip", 100, true, null)
        )
}

class PreviewListProvider : PreviewParameterProvider<List<Download>> {
    override val values: Sequence<List<Download>>
        get() = sequenceOf(
            listOf(
                Download(1, "BASE_ADR_NEY_20231106_121036058.zip", 45, false, null),
                Download(4, "test_4.zip", 100, true, null),
                Download(
                    5,
                    "BASE_ADR_NEY_20231106_121036058.zip",
                    3,
                    true,
                    true to "Failed to download item"
                ),
                Download(7, "test_98.zip", 75, false, null),
                Download(5, "BASE_ADR_NEY_20231106_121036058.zip", 100, true, null)
            )
        )
}