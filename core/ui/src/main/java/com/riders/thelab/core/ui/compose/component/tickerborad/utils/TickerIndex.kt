package com.riders.thelab.core.ui.compose.component.tickerborad.utils

@JvmInline
value class TickerIndex private constructor(private val packedIndex: Int) {
    val index: Int
        get() = (packedIndex and 0xFFFF0000.toInt()) shr 16

    val offsetIndex: Int
        get() = packedIndex and 0x0000FFFF

    companion object {
        operator fun invoke(
            rawIndex: Int,
            offsetIndex: Int,
        ) = TickerIndex(
            ((rawIndex and 0x0000FFFF) shl 16) + (offsetIndex and 0x0000FFFF)
        )
    }
}