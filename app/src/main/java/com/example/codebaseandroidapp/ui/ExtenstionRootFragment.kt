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

class ExtenstionRootFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_extenstion_root, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ExtenstionRootFragment().apply {

            }
    }
}