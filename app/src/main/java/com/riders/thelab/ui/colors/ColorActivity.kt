package com.riders.thelab.ui.colors

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabAnimationsManager
import com.riders.thelab.core.utils.LabColorsManager
import com.riders.thelab.databinding.ActivityColorBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ColorActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityColorBinding

    lateinit var colors: IntArray
    var fromColor = 0
    var toColor = 0
    var randomColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityColorBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        colors = LabColorsManager.getDefaultColors(this)

        viewBinding.changeColorButton.setOnClickListener {
            // Get a random color
            randomColor = colors[Random().nextInt(colors.size)]

            // Set as target color to use in animation
            toColor = randomColor
            // Get current TextView color
            fromColor = viewBinding.targetColorTextView.currentTextColor
            // Apply fade color transition to TextView
            LabAnimationsManager
                .getInstance()
                .applyFadeColorAnimationToView(
                    viewBinding.targetColorTextView,
                    fromColor,
                    toColor,
                    LabAnimationsManager
                        .getInstance()
                        .shortAnimationDuration
                )

            // Get current button color
            fromColor = viewBinding.changeColorButton.currentTextColor
            // Apply fade color transition to Button
            LabAnimationsManager
                .getInstance()
                .applyFadeColorAnimationToView(
                    viewBinding.changeColorButton,
                    fromColor,
                    toColor,
                    LabAnimationsManager
                        .getInstance()
                        .shortAnimationDuration
                )

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }
}