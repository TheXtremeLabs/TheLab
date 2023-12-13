package com.riders.thelab.core.data.remote.dto.kat

import com.google.firebase.Timestamp
import java.io.Serializable

data class KatMessage(
    val message: String,
    val senderId: String,
    val timestamp: Timestamp
) : Serializable {
    constructor() : this("", "", Timestamp.now())
}