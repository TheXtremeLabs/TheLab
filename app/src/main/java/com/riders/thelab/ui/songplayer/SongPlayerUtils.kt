package com.riders.thelab.ui.songplayer

class SongPlayerUtils {
    companion object {

        /**
         * Function to get Progress percentage
         * @param currentDuration
         * @param totalDuration
         */
        fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {
            val currentSeconds: Long = (currentDuration / 1000)
            val totalSeconds: Long = (totalDuration / 1000)

            // calculating percentage
            val percentage = currentSeconds.toDouble() / totalSeconds * 100

            // return percentage
            return percentage.toInt()
        }

        /**
         * Function to change progress to timer
         * @param progress -
         * @param totalDuration
         * returns current duration in milliseconds
         */
        fun progressToTimer(progress: Int, totalDuration: Int): Int {
            var mTotalDuration = totalDuration
            mTotalDuration = (mTotalDuration / 1000)
            val currentDuration: Int = (progress.toDouble() / 100 * mTotalDuration).toInt()

            // return current duration in milliseconds
            return currentDuration * 1000
        }

    }
}