package com.riders.thelab.utils;

import java.util.Random;

public class NumberUtils {

    private NumberUtils() {
    }

    public static int generateRandom(int max, int min) {
        // This gives you a random number in between 10 (inclusive) and 100 (exclusive)
        /*Random r = new Random();
        int low = 10;
        int high = 100;
        int randomResult = r.nextInt(high-low) + low;*/

        // This gives you a random number in between min (inclusive) and max (inclusive)
        return new Random().nextInt(max - min) + min;
    }

}
