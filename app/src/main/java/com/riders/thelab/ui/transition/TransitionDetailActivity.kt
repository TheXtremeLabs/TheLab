package com.riders.thelab.ui.transition

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.databinding.ActivityTransitionDetailBinding

class TransitionDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var _viewBinding: ActivityTransitionDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityTransitionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonDetail.setOnClickListener(this)
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

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    override fun onClick(view: View?) {
        onBackPressed()
    }
}