package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentHomeRootBinding

class HomeRootFragment : BaseFragment<FragmentHomeRootBinding>(FragmentHomeRootBinding::inflate) {

    override fun initialize() {
    }

    override fun initObserve() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeRootFragment()
    }
}