package com.riders.thelab.core.data.remote.dto.flight

/**
 * C when the aircraft is climbing, D when descending, and - when the altitude is being maintained.
 * Allowed: C┃D┃-
 */
enum class AltitudeChange (val value:String) {
    C("C"), D("D"), MAINTAINED("-");
}