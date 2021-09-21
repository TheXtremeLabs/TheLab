package com.riders.thelab.data.local.model

import java.io.File

sealed class Download {
    data class Started(val started: Boolean) : Download()
    data class Progress(val percent: Int) : Download()
    data class Done(val done: Boolean) : Download()
    data class Error(val error: Boolean) : Download()
    data class Finished(val file: File) : Download()
}
