package com.example.codebaseandroidapp.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.ui.SearchFragment

abstract class MainBaseFragment<T: ViewBinding>
    (private val inflateMethod : (LayoutInflater, ViewGroup?, Boolean) -> T) : BaseFragment<T>(inflateMethod) {

    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(requireActivity() is MainActivity) {
                        (requireActivity() as MainActivity).onBackPress()
                    }
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}