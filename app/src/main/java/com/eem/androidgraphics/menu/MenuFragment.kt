package com.eem.androidgraphics.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eem.androidgraphics.databinding.FragmentMenuBinding
import com.eem.androidgraphics.util.replaceFragment
import com.eem.androidgraphics.week1.DrawingBasicFragment

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.btnWeek1.setOnClickListener {
            replaceFragment(DrawingBasicFragment.newInstance())
        }

        return binding.root
    }

    companion object {
        fun newInstance() = MenuFragment()
    }
}