package com.riders.thelab.ui.transition

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.databinding.ActivityTransitionDetailBinding

class TransitionDetailActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityTransitionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTransitionDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.buttonDetail.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}