package com.riders.thelab.feature.transitions.xml

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.riders.thelab.core.ui.compose.base.BaseAppCompatActivity
import com.riders.thelab.feature.transitions.databinding.ActivityTransitionDetailBinding
import timber.log.Timber

class TransitionDetailActivity : BaseAppCompatActivity(), View.OnClickListener {

    private var _viewBinding: ActivityTransitionDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityTransitionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonDetail.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                backPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun backPressed() {
        Timber.e("backPressed() | call finishAfterTransition method")
        finishAfterTransition()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onClick(view: View?) {
        onBackPressed()
    }
}