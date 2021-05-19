package com.riders.thelab.ui.tabs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.riders.thelab.databinding.FragmentThreeBinding

class TwoFragment : Fragment() {
    companion object {
        fun newInstance(): TwoFragment {
            val args = Bundle()
            val fragment = TwoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentThreeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}