package com.example.codebaseandroidapp.ui

import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.MainBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentExtensionBinding

class ExtensionFragment : MainBaseFragment<FragmentExtensionBinding>(FragmentExtensionBinding::inflate) {

    override fun initObserve() {
    }

    override fun initialize() {
        binding.btnService.setOnClickListener {
            navController.navigate(R.id.action_extenstionFragment_to_serviceFragment)
        }
        binding.btnWorkManager.setOnClickListener {
            navController.navigate(R.id.action_extenstionFragment_to_workManagerFragment)
        }
        binding.btnSolid.setOnClickListener {
            navController.navigate(R.id.action_extenstionFragment_to_demoSolidFragment)
        }
        binding.btnThread.setOnClickListener {
            navController.navigate(R.id.action_extenstionFragment_to_demoThreadProcessFragment)
        }
        binding.btnRx.setOnClickListener {
            navController.navigate(R.id.action_extenstionFragment_to_demoReactiveExtensionFragment)
        }
    }

    companion object {
       @JvmStatic
        fun newInstance() = ExtensionFragment()
    }
}