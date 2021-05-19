package com.riders.thelab.ui.tabs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.riders.thelab.databinding.FragmentTwoBinding

class ThreeFragment : Fragment() {
    companion object {
        fun newInstance(): ThreeFragment {
            val args = Bundle()
            val fragment = ThreeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentTwoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}