package com.example.codebaseandroidapp.ui.Coroutine

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineMulRequestSequentialBinding
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineSingleRequestBinding
import com.example.codebaseandroidapp.databinding.FragmentDemoThreadProcessBinding
import com.example.codebaseandroidapp.utils.Utils.Companion.observer
import com.example.codebaseandroidapp.viewModel.DemoCoroutineMulRequestSequentialViewModel
import com.example.codebaseandroidapp.viewModel.DemoCoroutineSingleRequestViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoCoroutineMulRequestSequentialFragment : OthersBaseFragment<FragmentDemoCoroutineMulRequestSequentialBinding>(FragmentDemoCoroutineMulRequestSequentialBinding::inflate) {

    private val viewModel: DemoCoroutineMulRequestSequentialViewModel by viewModels()
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