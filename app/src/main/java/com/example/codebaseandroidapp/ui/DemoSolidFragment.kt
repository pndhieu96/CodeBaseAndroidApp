package com.example.codebaseandroidapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoSolidBinding

class DemoSolidFragment : OthersBaseFragment<FragmentDemoSolidBinding>(FragmentDemoSolidBinding::inflate) {

    companion object {
        @JvmStatic fun newInstance() = DemoSolidFragment()
    }

    override fun initObserve() {

    }

    override fun initialize() {
        
    }
}