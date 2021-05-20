package com.riders.thelab.ui.mainactivity

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter constructor(
    val activity: MainActivity,
    private val fragmentList: List<Fragment>
) : FragmentStateAdapter(activity) {

    private val mFragmentList: MutableList<Fragment> = fragmentList as MutableList<Fragment>

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return if (mFragmentList.isNotEmpty()) mFragmentList.size else 0
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}