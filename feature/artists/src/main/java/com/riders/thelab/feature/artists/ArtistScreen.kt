package com.riders.thelab.feature.artists

@kotlinx.serialization.Serializable
sealed class ArtistScreen(val route: String) {

    @kotlinx.serialization.Serializable
    data object List : ArtistScreen("list")

    @kotlinx.serialization.Serializable
    data class Detail(val id: Int) : ArtistScreen("details/{id}")
}