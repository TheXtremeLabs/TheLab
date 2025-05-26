package com.riders.thelab.feature.mlkit.data.local.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock

@Immutable
@Stable
data class TextRecognitionModel(
    val text: String,
    val blocks: List<TextBlock>,
    val lines: List<List<Text.Line>>
) {
    override fun toString(): String {
        return "TextRecognitionModel(\n" +
                "text: $text,\n" +
                "blocks: ${blocks.joinToString { block -> block.text + ", " }},\n" +
                "lines: ${lines.joinToString { line -> line.joinToString { it.text } }}\n" +
                ")"
    }
}
