package com.example.codebaseandroidapp.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseFragment<T: ViewBinding>
    (private val inflateMethod : (LayoutInflater, ViewGroup?, Boolean) -> T) : Fragment() {

    private var _binding : T? = null
    val binding: T get() = _binding!!
    val navController: NavController by lazy { NavHostFragment.findNavController(this) }
    var isInitView = AtomicBoolean(false)

    abstract fun initialize()
    abstract fun initObserve()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(_binding == null) {
            _binding = inflateMethod.invoke(inflater, container, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        if(isInitView.getAndSet(true).not())
            initialize()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}