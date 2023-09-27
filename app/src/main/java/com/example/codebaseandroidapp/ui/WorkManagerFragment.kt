package com.example.codebaseandroidapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentWorkManagerBinding
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.KEY_IMAGE_URI
import com.example.codebaseandroidapp.viewModel.WorkManagerViewModel
import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.base.OthersBaseFragment

class WorkManagerFragment : OthersBaseFragment<FragmentWorkManagerBinding>(FragmentWorkManagerBinding::inflate) {

    private val viewModel: WorkManagerViewModel by viewModels()
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WorkManager-5
        // Đăng ký observe để lắng nghe trạng thái và kết quả trả về từ 1 worker
        viewModel.outputWorkInfos.observe(this, workInfosObserver())
    }

    override fun initObserve() {
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun initialize() {
        requestPermissions()
        binding.let {
            it.goButton.setOnClickListener { viewModel.applyBlur(blurLevel) }
            it.seeFileButton.setOnClickListener {
                viewModel.outputUri?.let { currentUri ->
                    val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                    actionView.resolveActivity(requireActivity().packageManager)?.run {
                        startActivity(actionView)
                    }
                }
            }
            it.cancelButton.setOnClickListener {
                viewModel.cancelWork()
            }
        }
    }

    private fun showWorkInProgress() {
        //Dùng with để gán dữ liệu cho các thuôc tính của 1 đối tượng
        binding.let {
            it.progressBar.visibility = View.VISIBLE
            it.cancelButton.visibility = View.VISIBLE
            it.goButton.visibility = View.GONE
            it.seeFileButton.visibility = View.GONE
        }
    }

    private fun showWorkFinished() {
        binding.let {
            it.progressBar.visibility = View.GONE
            it.cancelButton.visibility = View.GONE
            it.goButton.visibility = View.VISIBLE
        }
    }

    // Define the observer function
    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                showWorkFinished()

                val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)

                // If there is an output file show "See File" button
                if (!outputImageUri.isNullOrEmpty()) {
                    viewModel.setOutputUri(outputImageUri)
                    binding.seeFileButton.visibility = View.VISIBLE
                }
            } else {
                showWorkInProgress()
            }
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            binding.goButton.visibility = View.GONE
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            readFile()
        }
    }

    private fun readFile() {
        binding.goButton.visibility = View.VISIBLE
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile()
            } else {
                // Permission Denied
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        @JvmStatic
        fun newInstance() = WorkManagerFragment()
    }

    private val blurLevel: Int
        get() =
            when (binding.radioBlurGroup.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> 1
                R.id.radio_blur_lv_2 -> 2
                R.id.radio_blur_lv_3 -> 3
                else -> 1
            }
}