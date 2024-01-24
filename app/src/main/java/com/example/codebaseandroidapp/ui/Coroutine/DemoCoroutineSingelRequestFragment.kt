package com.example.codebaseandroidapp.ui.Coroutine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineSingleRequestBinding
import com.example.codebaseandroidapp.utils.Utils.Companion.observer
import com.example.codebaseandroidapp.viewModel.DemoCoroutineScopeViewModel
import com.example.codebaseandroidapp.viewModel.DemoCoroutineSingleRequestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoCoroutineSingelRequestFragment : OthersBaseFragment<FragmentDemoCoroutineSingleRequestBinding>(FragmentDemoCoroutineSingleRequestBinding::inflate) {

    private val viewModel: DemoCoroutineSingleRequestViewModel by viewModels()
    companion object {
        @JvmStatic fun newInstance() = DemoCoroutineSingelRequestFragment()
    }
    override fun initObserve() {
        viewModel.data.observer(viewLifecycleOwner,
            onSuccess = {
                binding.result.text = it
            },
            onLoading = {
                binding.result.text = "Loading..."
            },
            onError = {
                binding.result.text = it.statusMessage
            }
        )
    }

    override fun initialize() {
        binding.btnStart.setOnClickListener {
            viewModel.start(3, false)
        }
        binding.btnException.setOnClickListener {
            viewModel.start(2, true)
        }
        binding.btnCancel.setOnClickListener {
            viewModel.cancel()
        }
    }
}