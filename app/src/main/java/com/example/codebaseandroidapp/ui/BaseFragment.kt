package com.example.codebaseandroidapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding

open class BaseFragment<T: ViewBinding>
    (private val inflateMethod : (LayoutInflater, ViewGroup?, Boolean) -> T) : Fragment() {

    private var _binding : T? = null
    val binding: T get() = _binding!!
    val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    open fun T.initialize(){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(inflater, container, false)

        binding.initialize()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}