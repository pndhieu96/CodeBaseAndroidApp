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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRootFragment : Fragment() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_root, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun navToRootDestination() {
        navController?.popBackStack(R.id.searchFragment, false)

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchRootFragment().apply {
            }
    }
}