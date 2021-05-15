package com.riders.thelab.data.remote.dto.artist;

import com.squareup.moshi.Json;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Parcel
public class Artist {
    @Json(name = "artistName")
    String artistName;
    @Json(name = "firstName")
    String firstName;
    @Json(name = "secondName")
    String secondName;
    @Json(name = "lastName")
    String lastName;
    @Json(name = "dateOfBirth")
    String dateOfBirth;
    @Json(name = "origin")
    String origin;
    @Json(name = "debutes")
    String debutes;
    @Json(name = "activities")
    String activities;
    @Json(name = "urlThumb")
    String urlThumb;
    @Json(name = "description")
    String description;
}
