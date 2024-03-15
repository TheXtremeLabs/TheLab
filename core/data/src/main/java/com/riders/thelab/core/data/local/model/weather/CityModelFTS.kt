package com.riders.thelab.core.data.local.model.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "city_fts")
@Fts4(contentEntity = CityModel::class)
data class CityModelFTS(
    @PrimaryKey
    @ColumnInfo("rowid")
    var rowid: Int,
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
)

fun CityModelFTS.toModel(): CityModel = CityModel(
    uuid = UUID.randomUUID().toString(),
    name = this.name,
    country = this.country,
    state = this.state,
    longitude = this.longitude,
    latitude = this.latitude
)