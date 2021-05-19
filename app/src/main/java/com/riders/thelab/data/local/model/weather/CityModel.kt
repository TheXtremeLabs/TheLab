package com.riders.thelab.data.local.model.weather

import android.database.Cursor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.riders.thelab.data.remote.dto.weather.City

@Entity(tableName = "city")
data class CityModel constructor(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: Int = 0,

        @ColumnInfo(name = "name")
        var name: String? = null,

        @ColumnInfo(name = "state")
        var state: String? = null,

        @ColumnInfo(name = "country")
        var country: String? = null,

        @ColumnInfo(name = "longitude")
        var longitude: Double? = 0.0,

        @ColumnInfo(name = "latitude")
        var latitude: Double? = 0.0
) {
    constructor() : this(-1, "", "", "", 0.0, 0.0)

    @Ignore
    constructor(dtoCity: City) : this() {
        this.id = dtoCity.id
        this.name = dtoCity.name
        this.state = dtoCity.state
        this.country = dtoCity.country
        this.longitude = dtoCity.coordinates?.longitude
        this.latitude = dtoCity.coordinates?.latitude
    }

    @Ignore
    constructor(cursor: Cursor) : this() {
        this.name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        this.state = (cursor.getString(cursor.getColumnIndexOrThrow("state")))
        this.country = (cursor.getString(cursor.getColumnIndexOrThrow("country")))
        this.latitude = (cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")))
        this.longitude = (cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")))
    }

    override fun toString(): String {
        return "CityModel(id=$id, name=$name, state=$state, country=$country, longitude=$longitude, latitude=$latitude)"
    }
}
