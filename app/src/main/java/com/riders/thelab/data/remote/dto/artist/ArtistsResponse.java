package com.riders.thelab.data.remote.dto.artist;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.List;

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
