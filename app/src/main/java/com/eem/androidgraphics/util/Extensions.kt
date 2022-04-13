package com.eem.androidgraphics.util

import android.view.ViewGroup
import androidx.fragment.app.Fragment

fun Fragment.replaceFragment(
    newFragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = newFragment.toString()
) {
    (view?.parent as ViewGroup?)?.id?.let { viewId ->
        val transaction = parentFragmentManager.beginTransaction().replace(viewId, newFragment)
        if (addToBackStack) transaction.addToBackStack(tag)
        transaction.commit()
    }
}