package com.riders.thelab.ui.tabs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.riders.thelab.databinding.FragmentOneBinding

// Instances of this class are fragments representing a single
// object in our collection.
class WorkingFragment : Fragment() {

    companion object {

        private const val ARG_OBJ = "arg_obj"

        fun newInstance(position: Int): WorkingFragment {
            val fragment = WorkingFragment()
            val args = Bundle()
            args.putInt(ARG_OBJ, position)
            fragment.arguments = args
            return fragment
        }
    }

    private var _viewBinding: FragmentOneBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            bundle.getInt(ARG_OBJ).let { position ->
                binding.tv.text = "Position : $position"
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _viewBinding = null
    }
}