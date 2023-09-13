package com.example.codebaseandroidapp.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseFragment<T: ViewBinding>
    (private val inflateMethod : (LayoutInflater, ViewGroup?, Boolean) -> T) : Fragment() {

    private var _binding : T? = null
    val binding: T get() = _binding!!
    val navController: NavController by lazy { NavHostFragment.findNavController(this) }
    var isInitView = AtomicBoolean(false)

    abstract fun initObserve()
    abstract fun initialize()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")

        if(_binding == null) {
            _binding = inflateMethod.invoke(inflater, container, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")

        initObserve()
        if(isInitView.getAndSet(true).not())
            initialize()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
    }
}