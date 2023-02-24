package com.example.codebaseandroidapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentExtenstionBinding

class ExtenstionFragment : BaseFragment<FragmentExtenstionBinding>(FragmentExtenstionBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun FragmentExtenstionBinding.initialize() {
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