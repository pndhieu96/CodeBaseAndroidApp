package com.example.codebaseandroidapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentWorkManagerBinding
import com.example.codebaseandroidapp.viewModel.WorkManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkManagerFragment : Fragment() {

    private var binding : FragmentWorkManagerBinding? = null
    private val viewModel: WorkManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkManagerBinding.inflate(inflater)
        binding?.let {
            it.goButton.setOnClickListener { viewModel.applyBlur(blurLevel) }
        }
        return binding!!.root
    }

    private fun showWorkInProgress() {
        //Dùng with để gán dữ liệu cho các thuôc tính của 1 đối tượng
        binding?.let {
            it.progressBar.visibility = View.VISIBLE
            it.cancelButton.visibility = View.VISIBLE
            it.goButton.visibility = View.GONE
            it.seeFileButton.visibility = View.GONE
        }
    }

    private fun showWorkFinished() {
        binding?.let {
            it.progressBar.visibility = View.GONE
            it.cancelButton.visibility = View.GONE
            it.goButton.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WorkManagerFragment().apply {

            }
    }

    private val blurLevel: Int
        get() =
            when (binding?.radioBlurGroup?.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> 1
                R.id.radio_blur_lv_2 -> 2
                R.id.radio_blur_lv_3 -> 3
                else -> 1
            }
}