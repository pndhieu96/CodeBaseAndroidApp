package com.example.codebaseandroidapp.ui.Coroutine

import androidx.fragment.app.viewModels
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineMulRequestCocurrencyBinding
import com.example.codebaseandroidapp.databinding.FragmentDemoThreadProcessBinding
import com.example.codebaseandroidapp.utils.Utils.Companion.observer
import com.example.codebaseandroidapp.viewModel.DemoCoroutineMulRequestConcurrencyViewModel
import com.example.codebaseandroidapp.viewModel.DemoCoroutineMulRequestSequentialViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoCoroutineMulRequestConcurrencyFragment : OthersBaseFragment<FragmentDemoCoroutineMulRequestCocurrencyBinding>(FragmentDemoCoroutineMulRequestCocurrencyBinding::inflate) {

    private val viewModel: DemoCoroutineMulRequestConcurrencyViewModel by viewModels()

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