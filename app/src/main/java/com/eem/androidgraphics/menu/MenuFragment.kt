package com.eem.androidgraphics.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eem.androidgraphics.databinding.FragmentMenuBinding
import com.eem.androidgraphics.opengl.OpenGlActivity
import com.eem.androidgraphics.util.replaceFragment
import com.eem.androidgraphics.week1.DrawingBasicFragment
import com.eem.androidgraphics.week1.PlottingAndGraphsFragment
import com.eem.androidgraphics.week1.TransformationDrawingFragment
import com.eem.androidgraphics.week2.Basic3DFragment
import com.eem.androidgraphics.week2.Gimbal

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
            replaceFragment(Basic3DFragment.newInstance(Basic3DFragment.CUBE))
        }

        binding.btnWeek23dGimbal.setOnClickListener {
            replaceFragment(Basic3DFragment.newInstance(Basic3DFragment.GIMBAL))
        }

        binding.btnOpengl1.setOnClickListener {
            val intent = Intent(requireActivity(), OpenGlActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {
        fun newInstance() = MenuFragment()
    }
}