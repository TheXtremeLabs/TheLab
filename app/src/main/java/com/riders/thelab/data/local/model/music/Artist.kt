package com.riders.thelab.data.local.model.music

import com.riders.thelab.data.remote.dto.artist.Artist
import kotools.types.text.NotBlankString

data class Artist(
    var artistName: NotBlankString,
    var firstName: NotBlankString,
    var secondName: String,
    var lastName: NotBlankString,
    var dateOfBirth: NotBlankString,
    var origin: NotBlankString,
    var debutes: String,
    var activities: String,
    var urlThumb: NotBlankString,
    var description: String
)

fun com.riders.thelab.data.remote.dto.artist.Artist.toModel(): Artist = Artist(
    artistName,
    firstName,
    secondName,
    lastName,
    dateOfBirth,
    origin,
    debutes,
    activities,
    urlThumb,
    description
)
