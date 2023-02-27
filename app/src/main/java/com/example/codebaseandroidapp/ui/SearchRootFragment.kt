package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentSearchRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRootFragment : BaseFragment<FragmentSearchRootBinding>(FragmentSearchRootBinding::inflate) {

    override fun initObserve() {
    }

    override fun initialize() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchRootFragment()
    }
}