package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentExtensionRootBinding

class ExtensionRootFragment : BaseFragment<FragmentExtensionRootBinding>(FragmentExtensionRootBinding::inflate) {

    override fun initialize() {
    }

    override fun initObserve() {
    }

    companion object {

        @JvmStatic
        fun newInstance() = ExtensionRootFragment()
    }
}