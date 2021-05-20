package com.riders.thelab.ui.mainactivity.fragment.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.LabDeviceManager
import com.riders.thelab.databinding.FragmentBottomSheetDialogBinding
import timber.log.Timber

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var viewBinding: FragmentBottomSheetDialogBinding

    fun newInstance(): BottomSheetFragment {
        val args = Bundle()

        val fragment = BottomSheetFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated()")

        viewBinding.tvBottomBrand.text = LabDeviceManager.getBrand()
        viewBinding.tvBottomModel.text = LabDeviceManager.getModel()
        viewBinding.tvBottomScreenHeight.text =
            LabDeviceManager.getScreenHeight(activity).toString() + ""
        viewBinding.tvBottomScreenWidth.text =
            LabDeviceManager.getScreenWidth(activity).toString() + ""
        viewBinding.tvBottomVersion.text =
            LabDeviceManager.getSdkVersion().toString() + " " + LabCompatibilityManager.getOSName()
    }
}