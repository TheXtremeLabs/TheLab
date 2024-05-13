package com.riders.thelab.core.data.local.model.kat

import com.google.firebase.Timestamp
import com.riders.thelab.core.data.remote.dto.kat.FCDKatUser
import java.io.Serializable

/**
 * Representing FCM Kat User remote object to local object in order to store remote user info
 */
//@kotlinx.serialization.Serializable
data class KatUserModel(
    val userId: String?,
    val phone: String,
    val username: String,
    //@kotlinx.serialization.Serializable(with = FirebaseTimestampSerializer::class)
    val createdTimestamp: Timestamp,
    val fcmToken: String
) : Serializable


/**
 * Converts [KatUserModel] object to FCD (Firebase Cloud Datastore) [FCDKatUser] object
 * @return [FCDKatUser] object
 */
fun KatUserModel.toKatDto(): FCDKatUser =
    FCDKatUser(this.userId, this.phone, this.username, this.createdTimestamp, this.fcmToken)