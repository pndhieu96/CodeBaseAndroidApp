package com.example.codebaseandroidapp.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.ui.SearchFragment

abstract class OthersBaseFragment<T: ViewBinding>
    (private val inflateMethod : (LayoutInflater, ViewGroup?, Boolean) -> T) : BaseFragment<T>(inflateMethod) {

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    /**
                     * Navigation-6
                     * NavController
                     * Dùng navController.PopBackStack() để back về destination trước đó trong backStack
                     */
                    navController.popBackStack()
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}