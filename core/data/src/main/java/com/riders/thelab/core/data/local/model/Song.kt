package com.riders.thelab.core.data.local.model

import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.Serializable


/**
 * Json example
 *      {
 *          "cost_time":0.37100005149841,
 *          "status":{
 *              "code":0,
 *              "msg":"Success",
 *              "version":"1.0"
 *          },
 *          "result_type":0,
 *          "metadata":{
 *              "timestamp_utc":"2023-09-21 09:36:34",
 *              "music":[{
 *                  "genres":[
 *                      {"name":"Rap\/Hip Hop"}
 *                  ],
 *                  "title":"rodeo dr",
 *                  "external_ids":{},
 *                  "acrid":"6f1c7de21b04b0b5dfcf2dbe66e45ef6",
 *                  "duration_ms":176000,
 *                  "db_begin_time_offset_ms":0,
 *                  "db_end_time_offset_ms":11000,
 *                  "sample_begin_time_offset_ms":0,
 *                  "sample_end_time_offset_ms":11000,
 *                  "play_offset_ms":22874,
 *                  "result_from":3,
 *                  "label":"300 Entertainment",
 *                  "release_date":"2023-06-16",
 *                  "score":100,
 *                  "artists":[
 *                      {"name":"Gunna"}
 *                  ],
 *                  "external_metadata":{
 *                      "spotify":{
 *                          "track":{
 *                              "name":"rodeo dr",
 *                              "id":"2rjv9fXzpZDbB6lEeIoB3l"
 *                          },
 *                          "artists":[
 *                              {
 *                                  "name":"Gunna",
 *                                  "id":"2hlmm7s2ICUX0LVIhVFlZQ"
 *                              }
 *                          ],
 *                          "album":{
 *                              "name":"a Gift & a Curse",
 *                              "id":"5qmZefgh78fN3jsyPPlvuw"
 *                          }
 *                      },
 *                      "youtube":{
 *                          "vid":"MzpSw4Y2uiI"
 *                      },
 *                      "deezer":{
 *                          "track":{
 *                              "name":"rodeo dr",
 *                              "id":"2329875165"
 *                          },
 *                          "artists":[
 *                              {"name":"Gunna","id":4344192}
 *                          ],
 *                          "album":{
 *                              "name":"a Gift & a Curse",
 *                              "id":453946145
 *                          }
 *                      }
 *                  },
 *                  "album":{"name":"rodeo dr"}
 *              }]
 *          }
 *      }
 */

@kotlinx.serialization.Serializable
data class Song(
    val genres: Set<String>,
    val title: String,
    val artists: Set<String>,
    val label: String,
    val releaseDate: String,
    val album: String,
    val externalMetadata: HashMap<String, String>
) : Serializable {

    companion object {

        val mock = Song(buildSet { add("Rap/Hip-Hop") },
            "6 foot 7 foot",
            buildSet { add("Toonchi") },
            "Young Money Cash Money Billionaires",
            "2010-12-16",
            "Tha Carter IV",
            HashMap()
        )

        fun toModel(
            genres: JSONArray,
            title: String,
            artists: JSONArray,
            label: String,
            releaseDate: String,
            album: JSONObject,
            externalMetadata: JSONObject
        ): Song {
            val spotify = externalMetadata.getJSONObject("spotify")
            val track = spotify.getJSONObject("track")
            val trackName = track.getString("name")
            val trackID = track.getString("id")
            val albumName = album.getString("name")
            // val albumID = album.getString("id")

            return Song(
                genres = if (0 == genres.length()) emptySet() else buildSet(genres.length()) {
                    genres.toList()?.forEach { genre ->
                        add(genre)
                    }
                    /*for (i in 0..genres.length()) {
                        val genre = genres[i] as JSONObject
                        val name = genre.getString("name")
                        add(name)
                    }*/
                },
                title = title,
                artists = if (0 == artists.length()) emptySet() else buildSet(artists.length()) {
                    artists.toList()?.forEach { artist ->
                        add(artist)
                    }
                    /*for (i in 0..artists.length()) {
                        val artist = artists[i] as JSONObject
                        val name = artist.getString("name")
                        add(name)
                    }*/
                },
                label = label,
                releaseDate = releaseDate,
                album = albumName,
                externalMetadata = HashMap<String, String>().apply
                {
                    put("spotify_metadata", "spotify_metadata")
                    put("trackName", trackName)
                    put("trackID", trackID)
                }
            )
        }
    }
}


fun JSONArray.toList(): List<String>? = runCatching {
    mutableListOf<String>()
        .apply {
            for (i in 0 until this@toList.length()) {
                Timber.d("element: ${this@toList[i] as JSONObject}")
                val element = this@toList[i] as JSONObject
                val value = element.getString("name")
                add(value)
            }
        }
        .run {
            this.toList()
        }
}
    .onFailure {
        Timber.e("runCatching | onFailure | error caught class: ${it.javaClass.simpleName}, with message: ${it.message}")
    }
    .getOrNull()

