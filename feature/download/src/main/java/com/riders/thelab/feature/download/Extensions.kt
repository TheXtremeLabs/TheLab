package com.riders.thelab.feature.download

import java.util.Locale

fun String.getFilenameFromUrl(): String = this
    .substring(this.lastIndexOf('/') + 1)
    .run {
        this
            .substring(0, 1)
            .uppercase(Locale.getDefault()) + this.substring(1)
    }
