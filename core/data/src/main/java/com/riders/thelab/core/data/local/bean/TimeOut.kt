package com.riders.thelab.core.data.local.bean

enum class TimeOut(val value: Int) {
    TIME_OUT_READ(60),
    TIME_OUT_CONNECTION(60);

    companion object {
        fun getValue(value: TimeOut): Int = entries.first { it.name == value.name }.value
    }
}