package com.riders.thelab.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riders.thelab.core.common.utils.encodeToSha256
import timber.log.Timber
import java.io.Serializable

@kotlinx.serialization.Serializable
@Entity(tableName = "user")
data class User(
    @ColumnInfo("first_name")
    val firstname: String,
    @ColumnInfo("last_name")
    val lastname: String,
    @ColumnInfo("username")
    val username: String,
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("password")
    var password: String,
    @ColumnInfo("last_time_registered")
    val lastTimeRegistered: Long
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var _id: Long = 0L

    @ColumnInfo("isAdmin")
    var isAdmin: Boolean = false

    @ColumnInfo("logged")
    var logged: Boolean = false

    constructor() : this("", "", "", "", "", 0L)

    /*init {
        Timber.d("User | init method")
        this.password = password.encodeToSha256()
    }*/

    companion object {
        val mockUserForTests: List<User> = listOf(
            User("Jane", "Doe","JaneDoe345" ,"jane.doe@test.com","test1234".encodeToSha256(), 1702222514L),
            User("John", "Smith","JohnSmith27" ,"john.smith@test.com","test1234".encodeToSha256(), 1702222522L),
            User("Mike", "Law","Mike1552" ,"mike@test.fr","test1234".encodeToSha256(), 1702222536L)
        )
    }
}
