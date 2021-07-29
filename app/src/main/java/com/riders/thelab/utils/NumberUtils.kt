package com.riders.thelab.utils

import java.util.*

class NumberUtils {
    companion object {

        fun generateRandom(max: Int, min: Int): Int {
            // This gives you a random number in between min (inclusive) and max (inclusive)
            return Random().nextInt(max - min) + min
        }
    }
}