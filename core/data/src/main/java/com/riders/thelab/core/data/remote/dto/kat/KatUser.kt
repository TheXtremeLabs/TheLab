package com.riders.thelab.core.data.remote.dto.kat

import com.google.firebase.Timestamp
import java.io.Serializable

@kotlinx.serialization.Serializable
data class KatUserAuth(val email: String, val password: String) : Serializable {
    constructor() : this("", "")
}

data class KatUser(
    val userId: String?,
    val phone: String,
    val username: String,
    val createdTimestamp: Timestamp,
    val fcmToken: String
) : Serializable {
    constructor() : this("", "", "", Timestamp.now(), "")
}

