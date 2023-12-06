package com.riders.thelab.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@kotlinx.serialization.Serializable
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val _id: Long,
    @ColumnInfo("first_name")
    val firstname: String,
    @ColumnInfo("last_name")
    val lastname: String,
    @ColumnInfo("username")
    val username: String,
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("password")
    val password: String,
    @ColumnInfo("last_time_registered")
    val lastTimeRegistered: Long
) : Serializable {
    constructor() : this(0L, "", "", "", "", "", 0L)
}
