package com.riders.thelab.core.data.remote.dto.kat

import java.io.Serializable

@kotlinx.serialization.Serializable
data class PushNotification(val data: NotificationData, val to :String): Serializable
