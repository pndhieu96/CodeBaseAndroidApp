package com.example.codebaseandroidapp.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentExtenstionBinding

class ExtenstionFragment : BaseFragment<FragmentExtenstionBinding>(FragmentExtenstionBinding::inflate) {

    override fun initObserve() {

    }

    override fun initialize() {
        binding.btnService.setOnClickListener {
            navController.navigate(R.id.action_extenstionFragment_to_serviceFragment)
        }
        binding.btnWorkManager.setOnClickListener {
            navController.navigate(R.id.action_extenstionFragment_to_workManagerFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(requireActivity() is MainActivity) {
                        (requireActivity() as MainActivity).onBackPress()
                    }
                }
            }
        )
    }

    companion object {
       @JvmStatic
        fun newInstance() = ExtenstionFragment()
    }
}