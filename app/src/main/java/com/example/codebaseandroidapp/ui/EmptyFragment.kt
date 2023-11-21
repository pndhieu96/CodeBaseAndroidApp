package com.example.codebaseandroidapp.ui

import android.util.Log
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoThreadProcessBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmptyFragment : OthersBaseFragment<FragmentDemoThreadProcessBinding>(FragmentDemoThreadProcessBinding::inflate) {

    companion object {
        @JvmStatic fun newInstance() = EmptyFragment()
    }

    override fun initObserve() {

    }

    override fun initialize() {

    }
}