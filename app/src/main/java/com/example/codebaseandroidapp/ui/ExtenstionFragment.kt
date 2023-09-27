package com.example.codebaseandroidapp.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.base.MainBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentExtenstionBinding

class ExtenstionFragment : MainBaseFragment<FragmentExtenstionBinding>(FragmentExtenstionBinding::inflate) {

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
    }

    companion object {
       @JvmStatic
        fun newInstance() = ExtenstionFragment()
    }
}