package com.riders.thelab.core.data.remote.dto.kat

import com.google.firebase.Timestamp
import com.riders.thelab.core.data.local.model.kat.KatUserModel
import java.io.Serializable


/**
 * Representing FCD (Firebase Cloud Datastore) Kat User remote object of user info
 */
data class FCDKatUser(
    val userId: String?,
    val phone: String,
    val username: String,
    val createdTimestamp: Timestamp,
    val fcmToken: String
) : Serializable


/**
 * Converts FCD (Firebase Cloud Datastore) [FCDKatUser] object to [KatUserModel] object
 * @return [KatUserModel] object
 */
fun FCDKatUser.toModel(): KatUserModel =
    KatUserModel(this.userId, this.phone, this.username, this.createdTimestamp, this.fcmToken)