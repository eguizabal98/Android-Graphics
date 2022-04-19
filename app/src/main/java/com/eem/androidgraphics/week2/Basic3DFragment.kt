package com.eem.androidgraphics.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Basic3DFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return My3DView(requireContext())
    }

    companion object {
        fun newInstance() = Basic3DFragment()
    }
}