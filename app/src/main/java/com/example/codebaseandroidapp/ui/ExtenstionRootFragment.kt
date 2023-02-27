package com.example.codebaseandroidapp.ui

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentExtenstionBinding
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