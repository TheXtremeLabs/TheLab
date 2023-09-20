package com.riders.thelab.core.common.utils

import java.util.Random

class NumberUtils {
    companion object {

        fun generateRandom(max: Int, min: Int): Int {
            // This gives you a random number in between min (inclusive) and max (inclusive)
            return Random().nextInt(max - min) + min
        }
    }
}