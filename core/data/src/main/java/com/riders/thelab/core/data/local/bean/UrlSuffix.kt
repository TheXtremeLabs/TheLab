package com.riders.thelab.core.data.local.bean

enum class UrlSuffix(val value: String) {
    MOVIES_URL_SUFFIX("/json/glide.json");

    companion object {
        fun getValue(value: UrlSuffix): String = entries.first { it.name == value.name }.value
    }
}