package com.example.codebaseandroidapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.viewModel.HomeRootViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeRootFragment : Fragment() {

    private val viewModel: HomeRootViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LC-HomeRootFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LC-HomeRootFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LC-HomeRootFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_home_root, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LC-HomeRootFragment", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LC-HomeRootFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LC-HomeRootFragment", "onResume")
    }

    override fun onPause() {
        Log.d("LC-HomeRootFragment", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("LC-HomeRootFragment", "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("LC-HomeRootFragment", "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("LC-HomeRootFragment", "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("LC-HomeRootFragment", "onDetach")
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeRootFragment().apply {

            }
    }
}