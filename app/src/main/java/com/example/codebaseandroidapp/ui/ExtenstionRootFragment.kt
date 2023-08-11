package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentExtenstionRootBinding

class ExtenstionRootFragment : BaseFragment<FragmentExtenstionRootBinding>(FragmentExtenstionRootBinding::inflate) {

    override fun initialize() {
    }

    override fun initObserve() {
    }

    companion object {

        @JvmStatic
        fun newInstance() = ExtenstionRootFragment()
    }
}