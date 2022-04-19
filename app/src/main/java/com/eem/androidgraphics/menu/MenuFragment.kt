package com.eem.androidgraphics.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eem.androidgraphics.databinding.FragmentMenuBinding
import com.eem.androidgraphics.util.replaceFragment
import com.eem.androidgraphics.week1.DrawingBasicFragment
import com.eem.androidgraphics.week1.PlottingAndGraphsFragment
import com.eem.androidgraphics.week1.TransformationDrawingFragment
import com.eem.androidgraphics.week2.Basic3DFragment

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.btnWeek1.setOnClickListener {
            replaceFragment(DrawingBasicFragment.newInstance())
        }

        binding.btnWeek1Transformation.setOnClickListener {
            replaceFragment(TransformationDrawingFragment.newInstance())
        }

        binding.btnWeek1Plots.setOnClickListener {
            replaceFragment(PlottingAndGraphsFragment.newInstance())
        }

        binding.btnWeek23dBasic.setOnClickListener {
            replaceFragment(Basic3DFragment.newInstance())
        }

        return binding.root
    }

    companion object {
        fun newInstance() = MenuFragment()
    }
}