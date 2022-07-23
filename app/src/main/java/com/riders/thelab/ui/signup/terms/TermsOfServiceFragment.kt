package com.riders.thelab.ui.signup.terms

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.riders.thelab.R
import com.riders.thelab.databinding.FragmentTermsOfServiceBinding
import com.riders.thelab.ui.signup.NextViewPagerClickListener
import timber.log.Timber

class TermsOfServiceFragment : Fragment(),
    View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var _viewBinding: FragmentTermsOfServiceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

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
        _viewBinding = FragmentTermsOfServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
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
        binding.cbAcceptLicense.setOnCheckedChangeListener(this)
    }

    private fun changeButtonState(isEnable: Boolean) {
        Timber.d("changeButtonState() to $isEnable")
        binding.btnContinue.isEnabled = isEnable

        binding.btnContinue.backgroundTintList =
            ColorStateList.valueOf(
                if (!isEnable) ContextCompat.getColor(
                    requireActivity(),
                    R.color.transparent
                ) else ContextCompat.getColor(
                    requireActivity(),
                    R.color.purple_500
                )
            )

        binding.btnContinue.setTextColor(
            if (!isEnable) ContextCompat.getColor(
                requireActivity(),
                R.color.jumbo
            ) else ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        )
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onClick(view: View?) {
        Timber.d("onContinueButtonClicked()")

        if (!binding.cbAcceptLicense.isChecked) {
            Timber.d("checkbox not checked display toast message")
            Toast.makeText(
                context,
                context!!.getString(R.string.err_msg_license_agreement_approval_mandatory),
                Toast.LENGTH_LONG
            )
                .show()
            return
        }

        Timber.d("user agreed go to the next activity")
        mListener.onNextViewPagerClicked()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        changeButtonState(isChecked)
    }

    companion object {
        val TAG: String = TermsOfServiceFragment::class.java.simpleName

        fun newInstance(): TermsOfServiceFragment {
            val args = Bundle()
            val fragment = TermsOfServiceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}