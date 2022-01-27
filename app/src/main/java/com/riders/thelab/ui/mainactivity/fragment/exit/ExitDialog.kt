package com.riders.thelab.ui.mainactivity.fragment.exit

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.riders.thelab.R
import com.riders.thelab.databinding.DialogExitBinding
import timber.log.Timber


class ExitDialog(private val mContext: Context) : Dialog(mContext), View.OnClickListener {

    private var _viewBinding: DialogExitBinding? = null
    private val binding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = DialogExitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        Timber.e("setOnDismissListener()")
    }

    private fun setListeners() {
        binding.btnQuit.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_quit -> (mContext as Activity).finish()
            R.id.btn_cancel -> dismiss()
            else -> {
                Timber.e("else branch")
            }
        }
        dismiss()
    }

}