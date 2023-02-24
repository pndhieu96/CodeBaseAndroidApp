package com.example.codebaseandroidapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentExtenstionBinding
import com.example.codebaseandroidapp.databinding.FragmentExtenstionRootBinding

class ExtenstionRootFragment : BaseFragment<FragmentExtenstionRootBinding>(FragmentExtenstionRootBinding::inflate) {

    companion object {

        @JvmStatic
        fun newInstance() = ExtenstionRootFragment()
    }
}