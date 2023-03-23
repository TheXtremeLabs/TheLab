package com.riders.thelab.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivitySignUpBinding
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.signup.successfulsignup.SuccessfulSignUpFragment
import com.riders.thelab.ui.signup.terms.TermsOfServiceFragment
import com.riders.thelab.ui.signup.userform.UserFormFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
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
    private var pagerAdapter: ViewPager2Adapter? = null
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

        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }

        initViews()
        setListeners()
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
    }

    private fun initViewModelsObservers() {
        Timber.d("initViewModelsObservers()")
    }

    private fun setListeners() {
        Timber.d("setListeners()")
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {
                    0 -> {
                        Timber.e("Remove previous")
                        removeToolbarUserForm()
                    }
                    1 -> {
                        updateToolbarUserForm()
                    }
                    2 -> {
                        updateToolbarSuccessful()
                        if (2 == binding.viewPager.currentItem) {

                            val successfulFragmentInstance: SuccessfulSignUpFragment =
                                pagerAdapter?.getFragment(position) as SuccessfulSignUpFragment

                            successfulFragmentInstance.saveUser()
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("NewApi")
    private fun updateToolbarUserForm() {
        Timber.d("updateToolbarUserForm()")
        if (binding.includeToolbarSignUpLayout.progressBarUserForm.progress == 0) {
            binding.includeToolbarSignUpLayout.progressBarUserForm.setProgress(100, true)
        }

        if (ContextCompat.getColor(
                this@SignUpActivity,
                R.color.white
            ) != binding.includeToolbarSignUpLayout.tvUserForm.textColors.defaultColor
        ) {
            binding.includeToolbarSignUpLayout.tvUserForm.setTextColor(
                ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.white
                )
            )
        }
    }

    @SuppressLint("NewApi")
    private fun removeToolbarUserForm() {
        Timber.d("removeToolbarUserForm()")
        if (binding.includeToolbarSignUpLayout.progressBarUserForm.progress == 100) {
            binding.includeToolbarSignUpLayout.progressBarUserForm.setProgress(0, true)
        }

        if (ContextCompat.getColor(
                this@SignUpActivity,
                R.color.white
            ) == binding.includeToolbarSignUpLayout.tvUserForm.textColors.defaultColor
        ) {
            binding.includeToolbarSignUpLayout.tvUserForm.setTextColor(
                ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.jumbo
                )
            )
        }
    }

    @SuppressLint("NewApi")
    private fun updateToolbarSuccessful() {
        Timber.d("updateToolbarSuccessful()")
        if (binding.includeToolbarSignUpLayout.progressBarSuccessful.progress == 0) {
            binding.includeToolbarSignUpLayout.progressBarSuccessful.setProgress(100, true)
        }

        if (ContextCompat.getColor(
                this@SignUpActivity,
                R.color.white
            ) != binding.includeToolbarSignUpLayout.tvSuccessful.textColors.defaultColor
        ) {
            binding.includeToolbarSignUpLayout.tvSuccessful.setTextColor(
                ContextCompat.getColor(
                    this@SignUpActivity,
                    R.color.white
                )
            )
        }
    }

    override fun onNextViewPagerClicked() {
        Timber.d("onNextViewPagerClicked()")
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    override fun onLastViewPagerClicked() {
        binding.viewPager.setCurrentItem(pagerAdapter?.itemCount?.minus(1)!!, true)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onFinishSignUp() {
        Navigator(this).callMainActivity()
        finish()
    }
}