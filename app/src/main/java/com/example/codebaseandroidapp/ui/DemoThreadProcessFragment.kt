package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoThreadProcessBinding
import dagger.hilt.android.AndroidEntryPoint

/*
* Thread:
*
* Process:
*
* Multi Thread:
*
*
* */

@AndroidEntryPoint
class DemoThreadProcessFragment : OthersBaseFragment<FragmentDemoThreadProcessBinding>(FragmentDemoThreadProcessBinding::inflate) {

    companion object {
        @JvmStatic fun newInstance() = DemoThreadProcessFragment()
    }

    override fun initObserve() {

    }

    override fun initialize() {

    }
}