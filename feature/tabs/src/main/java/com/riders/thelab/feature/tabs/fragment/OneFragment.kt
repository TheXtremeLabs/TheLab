package com.riders.thelab.feature.tabs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.riders.thelab.feature.tabs.databinding.FragmentOneBinding

class OneFragment : Fragment() {
    companion object {
        fun newInstance(): OneFragment {
            val args = Bundle()
            val fragment = OneFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentOneBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}