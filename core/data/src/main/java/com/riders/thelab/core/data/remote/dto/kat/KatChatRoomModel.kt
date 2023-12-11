package com.riders.thelab.core.data.remote.dto.kat

import com.google.firebase.Timestamp

data class KatChatRoomModel(
    val chatRoomId: String,
    val userIds: List<String>,
    val lastMessageTimestamp: Timestamp,
    val lastSenderId: String
) {
    constructor() : this("", emptyList(), Timestamp.now(), "")
}
