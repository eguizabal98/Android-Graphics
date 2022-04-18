package com.eem.androidgraphics.week1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DrawingBasicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DrawingBasicView(requireContext())
    }

    companion object {
        fun newInstance() = DrawingBasicFragment()
    }
}