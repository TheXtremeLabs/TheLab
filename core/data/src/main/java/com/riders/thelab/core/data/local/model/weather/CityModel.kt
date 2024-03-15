package com.riders.thelab.core.data.local.model.weather

import android.database.Cursor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.riders.thelab.core.data.remote.dto.weather.City
import java.util.UUID

@Entity(tableName = "city")
data class CityModel(
    @PrimaryKey
    @ColumnInfo("rowid")
    var id: Int,
    @ColumnInfo("uuid")
    var uuid: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "state")
    var state: String,
    @ColumnInfo(name = "country")
    var country: String,
    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0,
    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0
) {
    @Ignore
    constructor(dtoCity: City) : this(
        id = dtoCity.id.toInt(),
        uuid = UUID.randomUUID().toString(),
        name = dtoCity.name,
        state = dtoCity.state,
        country = dtoCity.country,
        latitude = dtoCity.coordinates.latitude,
        longitude = dtoCity.coordinates.longitude
    )

    @Ignore
    constructor(
        uuid: String,
        name: String,
        state: String,
        country: String,
        longitude: Double = 0.0, latitude: Double = 0.0
    ) : this(
        id = -1,
        uuid = uuid,
        name = name,
        state = state,
        country = country,
        longitude = longitude,
        latitude = latitude
    )

    @Ignore
    constructor(cursor: Cursor) : this(
        id = -1,
        uuid = UUID.randomUUID().toString(),
        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
        state = (cursor.getString(cursor.getColumnIndexOrThrow("state"))),
        country = (cursor.getString(cursor.getColumnIndexOrThrow("country"))),
        latitude = (cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))),
        longitude = (cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")))
    )
}

fun CityModel.toAppSearchModel(): CityAppSearch = CityAppSearch(
    id = UUID.randomUUID().toString(),
    score = 1,
    name = this.name,
    state = this.state,
    country = this.country,
    latitude = this.latitude,
    longitude = this.longitude
)