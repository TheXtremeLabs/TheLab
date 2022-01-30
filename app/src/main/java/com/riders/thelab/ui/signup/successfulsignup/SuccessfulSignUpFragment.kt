package com.riders.thelab.ui.signup.successfulsignup

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.riders.thelab.R
import com.riders.thelab.databinding.FragmentSuccessfulSignUpBinding
import com.riders.thelab.ui.signup.NextViewPagerClickListener
import com.riders.thelab.ui.signup.SignUpViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class SuccessfulSignUpFragment : Fragment(),
    CoroutineScope,
    View.OnClickListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


    private var _viewBinding: FragmentSuccessfulSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!


    private val mViewModel: SignUpViewModel by activityViewModels()


    private lateinit var mListener: NextViewPagerClickListener


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as NextViewPagerClickListener
        } catch (exception: ClassCastException) {
            throw ClassCastException(
                "${activity.toString()} must implement ${NextViewPagerClickListener::class.java.simpleName}"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSuccessfulSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initViewModelsObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun setListeners() {
        binding.btnContinue.setOnClickListener(this)
    }

    private fun initViewModelsObservers() {
        Timber.d("initViewModelsObservers()")

        mViewModel.getSaveUserSuccessful().observe(requireActivity()) {
            Timber.e("getSaveUserSuccessful().observe")

            binding.clSavingUserProcess.visibility = View.GONE
            binding.motionLayout.visibility = View.VISIBLE

            lifecycleScope.launch(coroutineContext) {
                delay(TimeUnit.SECONDS.toMillis(1))
                startChainedAnimations()
            }
        }

        mViewModel.getSaveUserError().observe(requireActivity()) {
            Timber.e("getSaveUserError().observe")
            Timber.e("Error - $it")
        }
    }


    fun saveUser() {
        Timber.d("saveUser()")
        mViewModel.saveUser()
    }

    private fun startChainedAnimations() {
        Timber.d("startChainedAnimations()")
        binding.motionLayout.setTransition(R.id.start, R.id.start_to_title)
        binding.motionLayout.transitionToEnd {
            binding.motionLayout.setTransition(R.id.start_to_title, R.id.title_to_message)
            binding.motionLayout.transitionToEnd {
                binding.motionLayout.setTransition(
                    R.id.title_to_message,
                    R.id.message_to_animated_image
                )
                binding.motionLayout.transitionToEnd {
                    val d: Drawable = binding.ivAnimatedCheck.drawable

                    if (d is AnimatedVectorDrawable) {
                        val mAnimationDrawable: AnimatedVectorDrawable = d
                        mAnimationDrawable.start()
                    }

                    binding.motionLayout.setTransition(R.id.message_to_animated_image, R.id.end)
                    binding.motionLayout.transitionToEnd {
                        Timber.d("Chained Animation complete")
                    }
                }
            }
        }
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_continue -> {
                mListener.onFinishSignUp()
            }
        }
    }

    companion object {
        val TAG: String = SuccessfulSignUpFragment::class.java.simpleName

        fun newInstance(): SuccessfulSignUpFragment {
            val args = Bundle()
            val fragment = SuccessfulSignUpFragment()
            fragment.arguments = args
            return fragment
        }
    }
}