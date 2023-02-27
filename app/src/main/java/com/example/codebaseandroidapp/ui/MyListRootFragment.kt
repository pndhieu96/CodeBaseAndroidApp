package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentMyListRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyListRootFragment : BaseFragment<FragmentMyListRootBinding>(FragmentMyListRootBinding::inflate) {

    override fun initObserve() {
    }

    override fun initialize() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyListRootFragment()
    }
}