package com.riders.thelab.ui.mainactivity

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.DelicateCoroutinesApi

class ViewPager2Adapter @OptIn(DelicateCoroutinesApi::class) constructor(
    val activity: MainActivity,
    private val fragmentList: List<Fragment>
) : FragmentStateAdapter(activity) {

    private val mFragmentList: MutableList<Fragment> = fragmentList as MutableList<Fragment>

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    fun getFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (mFragmentList.isNotEmpty()) mFragmentList.size else 0
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}