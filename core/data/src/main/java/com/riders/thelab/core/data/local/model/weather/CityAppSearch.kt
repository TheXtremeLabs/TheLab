package com.riders.thelab.core.data.local.model.weather

import androidx.appsearch.annotation.Document
import androidx.appsearch.annotation.Document.DoubleProperty
import androidx.appsearch.annotation.Document.Id
import androidx.appsearch.annotation.Document.Namespace
import androidx.appsearch.annotation.Document.Score
import androidx.appsearch.annotation.Document.StringProperty
import androidx.appsearch.app.AppSearchSchema

@Document
data class CityAppSearch(
    @Namespace
    val namespace: String = "weather_search",
    @Id
    val id: String,
    @Score
    val score: Int,
    @StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    val name: String,
    @StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    val state: String,
    @StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    val country: String,
    @DoubleProperty
    val longitude: Double = 0.0,
    @DoubleProperty
    val latitude: Double = 0.0
)
