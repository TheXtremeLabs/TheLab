package com.riders.thelab.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.riders.thelab.databinding.ActivitySignUpBinding
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.signup.successfulsignup.SuccessfulSignUpFragment
import com.riders.thelab.ui.signup.terms.TermsOfServiceFragment
import com.riders.thelab.ui.signup.userform.UserFormFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity(),
    CoroutineScope, NextViewPagerClickListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: ActivitySignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SignUpViewModel by viewModels()


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var pagerAdapter: FragmentStateAdapter? = null
    private var fragmentList: MutableList<Fragment>? = null

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initViewModelsObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////

    private fun initViews() {
        // Instantiate a ViewPager2 and a PagerAdapter.
        fragmentList = ArrayList()

        // add Fragments in your ViewPagerFragmentAdapter class
        fragmentList!!.add(TermsOfServiceFragment.newInstance())
        fragmentList!!.add(UserFormFragment.newInstance())
        fragmentList!!.add(SuccessfulSignUpFragment.newInstance())

        pagerAdapter = ViewPager2Adapter(this@SignUpActivity, fragmentList as ArrayList<Fragment>)

        // set Orientation in your ViewPager2
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.isUserInputEnabled = false

        binding.tabLayout.let { tabLayout ->
            binding.viewPager.let { viewPager2 ->
                TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                    //Some implementation
                }.attach()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModelsObservers() {
        Timber.d("initViewModelsObservers()")
    }

    override fun onNextViewPagerClicked() {
        Timber.d("onNextViewPagerClicked()")
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    override fun onLastViewPagerClicked() {
        binding.viewPager.setCurrentItem(pagerAdapter?.itemCount?.minus(1)!!, true)
    }

    override fun onFinishSignUp() {
        Navigator(this).callMainActivity()
        finish()
    }
}