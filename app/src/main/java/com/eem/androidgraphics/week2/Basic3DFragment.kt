package com.eem.androidgraphics.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class Basic3DFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return when (arguments?.getInt(VIEW_TYPE)) {
            CUBE -> My3DView(requireContext())
            GIMBAL -> Gimbal(requireContext())
            else -> My3DView(requireContext())
        }
    }

    companion object {
        fun newInstance(view: Int) = Basic3DFragment().apply {
            arguments = bundleOf(Pair(VIEW_TYPE, view))
        }

        const val VIEW_TYPE = "VIEW_TYPE"
        const val CUBE = 0
        const val GIMBAL = 1
    }
}