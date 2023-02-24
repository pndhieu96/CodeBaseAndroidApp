package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.databinding.FragmentSearchBinding
import com.example.codebaseandroidapp.databinding.FragmentSearchRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRootFragment : BaseFragment<FragmentSearchRootBinding>(FragmentSearchRootBinding::inflate) {

    companion object {
        @JvmStatic
        fun newInstance() = SearchRootFragment()
    }
}