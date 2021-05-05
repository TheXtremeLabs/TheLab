package com.riders.thelab.data.remote.dto;

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
    private String artistName;
    @Json(name = "firstName")
    private String firstName;
    @Json(name = "secondName")
    private String secondName;
    @Json(name = "lastName")
    private String lastName;
    @Json(name = "dateOfBirth")
    private String dateOfBirth;
    @Json(name = "origin")
    private String origin;
    @Json(name = "debutes")
    private String debutes;
    @Json(name = "activities")
    private String activities;
    @Json(name = "urlThumb")
    private String urlThumb;
    @Json(name = "description")
    private String description;
}
