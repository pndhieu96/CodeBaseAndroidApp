package com.example.codebaseandroidapp.ui.Coroutine

import androidx.fragment.app.viewModels
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineExceptionBinding
import com.example.codebaseandroidapp.viewModel.DemoCoroutineExceptionHandlerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoCoroutineExceptionHandler : OthersBaseFragment<FragmentDemoCoroutineExceptionBinding>(FragmentDemoCoroutineExceptionBinding::inflate) {

    private val viewModel: DemoCoroutineExceptionHandlerViewModel by viewModels()
    companion object {
        @JvmStatic fun newInstance() = DemoCoroutineExceptionHandler()
    }

    override fun initObserve() {

    }

    override fun initialize() {
        binding.btnDownloadAndSave.setOnClickListener {
            viewModel.getDataWithExceptionHandler()
        }
    }
}