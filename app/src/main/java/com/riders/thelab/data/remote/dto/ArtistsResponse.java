package com.riders.thelab.data.remote.dto;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ArtistsResponse {
    private List<Artist> artists;

    @FromJson
    List<Artist> artistsFromJson(List<Artist> artistsEventJson) {
        return artistsEventJson;
    }

    @ToJson
    String artistsToJson(List<Artist> artists) {
        return artists.toString();
    }
}
