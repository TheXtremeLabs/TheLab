package com.riders.thelab.feature.artists.data.mapper

import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.feature.artists.data.local.model.ArtistUi

fun Artist.toArtistUi(): ArtistUi = ArtistUi(
    index = this.index,
    artistName = this.artistName,
    firstName = this.firstName,
    secondName = this.secondName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    origin = this.origin,
    debutes = this.debutes,
    activities = this.activities,
    urlThumb = this.urlThumb,
    description = this.description
)

fun ArtistUi.toDomainArtist(): Artist = Artist(
    artistName = this.artistName,
    firstName = this.firstName,
    secondName = this.secondName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    origin = this.origin,
    debutes = this.debutes,
    activities = this.activities,
    urlThumb = this.urlThumb,
    description = this.description
).apply {
    this.index = this@toDomainArtist.index
}