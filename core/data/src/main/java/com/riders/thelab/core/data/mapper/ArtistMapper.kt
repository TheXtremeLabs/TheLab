package com.riders.thelab.core.data.mapper

import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.domain.model.artist.Artist

fun Artist.toModel(index: Byte): ArtistModel = ArtistModel(
    id = index,
    sceneName = artistName,
    firstName = firstName,
    secondName = secondName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    origin = origin.toString(),
    debutes = debutes.toString(),
    activities = activities.toString(),
    description = description.toString(),
    urlThumbnail = urlThumb,
    encodedThumbnail = thumbnailFullUrl.toString()
)

fun ArtistModel.toDomain(): Artist = Artist(
    artistName = sceneName,
    firstName = firstName,
    secondName = secondName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    origin = origin,
    debutes = debutes,
    activities = activities,
    urlThumb = urlThumbnail ?: "",
    description = description,
    thumbnailFullUrl = encodedThumbnail
).apply {
   if(((-1).toByte()) != this@toDomain.id){
       index = this@toDomain.id
   }
}

fun com.riders.thelab.core.data.remote.dto.artist.Artist.toDomain(): Artist = Artist(
    artistName = artistName,
    firstName = firstName,
    secondName = secondName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    origin = origin,
    debutes = debutes,
    activities = activities,
    urlThumb = urlThumb ?: "",
    description = description,
    thumbnailFullUrl = urlThumb
)