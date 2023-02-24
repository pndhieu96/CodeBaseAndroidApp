package com.example.codebaseandroidapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentMyListRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyListRootFragment : BaseFragment<FragmentMyListRootBinding>(FragmentMyListRootBinding::inflate) {

    companion object {
        @JvmStatic
        fun newInstance() =
            MyListRootFragment().apply {

            }
    }
}