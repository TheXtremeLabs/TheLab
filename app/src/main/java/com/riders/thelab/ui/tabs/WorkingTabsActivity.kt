package com.riders.thelab.ui.tabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityTabsBinding
import com.riders.thelab.ui.tabs.fragment.OneFragment
import com.riders.thelab.ui.tabs.fragment.ThreeFragment
import com.riders.thelab.ui.tabs.fragment.TwoFragment
import java.util.*

class WorkingTabsActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityTabsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTabsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.tabToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.activity_title_tabs)

        setupViewPager(viewBinding.tabViewPager)

        viewBinding.tabs.setupWithViewPager(viewBinding.tabViewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OneFragment.newInstance(), "ONE")
        adapter.addFragment(TwoFragment.newInstance(), "TWO")
        adapter.addFragment(ThreeFragment.newInstance(), "THREE")
        viewPager.adapter = adapter
    }


    private class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {

        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {

            // return null to display only the icon (if there's icons)
            return mFragmentTitleList[position]
        }
    }
}