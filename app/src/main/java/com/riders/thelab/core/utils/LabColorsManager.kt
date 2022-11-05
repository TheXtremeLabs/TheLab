package com.riders.thelab.core.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.riders.thelab.R

object LabColorsManager {
    fun getDefaultColors(context: Context): IntArray {
        return intArrayOf(
            ContextCompat.getColor(context, R.color.white),
            ContextCompat.getColor(context, R.color.red),
            ContextCompat.getColor(context, R.color.blue),
            ContextCompat.getColor(context, R.color.green),
            ContextCompat.getColor(context, R.color.orange),
            ContextCompat.getColor(context, R.color.purple),
            ContextCompat.getColor(context, R.color.yellow),
            ContextCompat.getColor(context, R.color.teal_700)
        )
    }
}