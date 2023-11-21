package com.example.codebaseandroidapp.ui

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineScopeBinding
import com.example.codebaseandroidapp.databinding.FragmentDemoThreadProcessBinding
import com.example.codebaseandroidapp.viewModel.DemoCoroutineScopeViewModel
import com.example.codebaseandroidapp.viewModel.HomeViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoCoroutineScopeFragment : OthersBaseFragment<FragmentDemoCoroutineScopeBinding>(FragmentDemoCoroutineScopeBinding::inflate) {

    private val viewModel: DemoCoroutineScopeViewModel by viewModels()
    companion object {
        @JvmStatic fun newInstance() = DemoCoroutineScopeFragment()
    }

    override fun initObserve() {

    }

    override fun initialize() {
        binding.btnStart.setOnClickListener {
            viewModel.start()
        }

        binding.btnStop.setOnClickListener {
            viewModel.stop()
        }

        binding.btnDetail.setOnClickListener {
            navController.navigate(R.id.action_demoCoroutineScopeFragment_to_demoCoroutineExceptionHandler)
        }
    }
}