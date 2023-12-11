package com.riders.thelab.core.data.remote.dto.kat

import com.google.firebase.Timestamp

data class KatUserAuth(val email: String, val password: String)
data class KatUser(val phone: String, val username: String, val createdTimestamp: Timestamp)
