package com.riders.thelab.feature.flightaware.utils

import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/////////////////////////////////////////////////////
// NotBlankString
/////////////////////////////////////////////////////
fun NotBlankString.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime =
    LocalDateTime.ofInstant(Instant.parse(this.toString()), zoneId)


fun NotBlankString.toFormattedDate(formatter: DateTimeFormatter): NotBlankString =
    if (this.toString() == "N/A") {
        "N/A".toNotBlankString().getOrThrow()
    } else {
        this
            .toLocalDateTime()
            .format(formatter)
            .toString()
            .toNotBlankString()
            .getOrThrow()
    }
