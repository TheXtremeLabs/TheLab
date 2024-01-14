package com.riders.thelab.core.data.local.model.tmdb

import com.riders.thelab.core.data.local.model.Actors
import com.riders.thelab.core.data.local.model.Director
import com.riders.thelab.core.data.local.model.Scenarist

data class TMDBItemModel(
    var category: String,
    var title: String,
    var originalTitle: String,
    var overview: String,
    var genre: String,
    var year: String,
    var urlThumbnail: String,
    var type: String,
    var duration: Int,
    var directors: List<Director>? = null,
    var scenarists: List<Scenarist>? = null,
    var cast: List<Actors>? = null,
    var popularity: Int,
    var rating: Double
)
