package com.riders.thelab.ui.transition

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityTransitionBinding

class TransitionActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityTransitionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w = window
        w.allowEnterTransitionOverlap = true

        viewBinding = ActivityTransitionBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.title = getString(R.string.activity_title_transition)

        viewBinding.buttonNextActivity.setOnClickListener {


            // Variables
            val intent = Intent(this, TransitionDetailActivity::class.java)
            var options: ActivityOptionsCompat? = null


            var sePairThumb: Pair<View, String> =
                Pair.create(viewBinding.ivLogo, getString(R.string.logo_transition_name))
            var sePairButton: Pair<View, String> =
                Pair.create(
                    viewBinding.buttonNextActivity,
                    getString(R.string.button_transition_name)
                )

            // create the transition animation - the images in the layouts
            // of both activities are defined with android:transitionName="robot"

            // create the transition animation - the images in the layouts
            // of both activities are defined with android:transitionName="robot"
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sePairThumb, sePairButton
            )

            // start the new activity

            // start the new activity
            startActivity(intent, options.toBundle())
        }
    }
}