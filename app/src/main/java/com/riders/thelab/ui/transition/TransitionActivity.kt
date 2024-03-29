package com.riders.thelab.ui.transition

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityTransitionBinding

class TransitionActivity : AppCompatActivity(), View.OnClickListener {

    private var _viewBinding: ActivityTransitionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w = window
        w.allowEnterTransitionOverlap = true

        _viewBinding = ActivityTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonNextActivity.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    override fun onClick(view: View?) {
        // Variables
        val intent = Intent(this, TransitionDetailActivity::class.java)

        val sePairThumb: Pair<View, String> =
            Pair.create(binding.ivLogo, getString(R.string.logo_transition_name))
        val sePairButton: Pair<View, String> =
            Pair.create(
                binding.buttonNextActivity,
                getString(R.string.button_transition_name)
            )

        // create the transition animation - the images in the layouts
        // of both activities are defined with android:transitionName="robot"
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            sePairThumb, sePairButton
        )

        // start the new activity
        startActivity(intent, options.toBundle())
    }
}