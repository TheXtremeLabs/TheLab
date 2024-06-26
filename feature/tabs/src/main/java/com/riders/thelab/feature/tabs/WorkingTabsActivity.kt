package com.riders.thelab.feature.tabs

import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.riders.thelab.core.ui.compose.base.BaseAppCompatActivity
import com.riders.thelab.feature.tabs.databinding.ActivityTabsBinding
import com.riders.thelab.feature.tabs.fragment.OneFragment
import com.riders.thelab.feature.tabs.fragment.ThreeFragment
import com.riders.thelab.feature.tabs.fragment.TwoFragment
import timber.log.Timber

class WorkingTabsActivity : BaseAppCompatActivity() {

    private var _viewBinding: ActivityTabsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityTabsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tabToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewPager(binding.tabViewPager2)

        TabLayoutMediator(binding.tabs, binding.tabViewPager2) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                backPressed()
            }
        }
        return true
    }

    override fun backPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }


    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter = WorkingTabsAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(OneFragment.newInstance(), "ONE")
        adapter.addFragment(TwoFragment.newInstance(), "TWO")
        adapter.addFragment(ThreeFragment.newInstance(), "THREE")
        viewPager.adapter = adapter
    }
}