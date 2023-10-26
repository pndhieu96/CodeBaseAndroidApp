package com.example.codebaseandroidapp.ui

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineBinding
import com.example.codebaseandroidapp.databinding.FragmentDemoSolidBinding
import com.example.codebaseandroidapp.di.FragmentModule
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Console
import java.io.Serializable
import javax.inject.Inject
import kotlin.math.E

/*
* Solid principles:
* S (Single responsibility principle): Mỗi class nên có 1 chức năng duy nhất
* O (Open and close principle): (Objects or entities should be open for extension,
*   but closed for modification) Một class nên được mở rộng chứ không nên thay
*   đổi bên trong nó.
* L (Liskov substitution principle): Các class con có thể thay thế class cha mà
*   không làm thay đổi tính đúng đắn.
* I (Interface segregation principle): Thay vì dùng 1 interface lớn, ta có thể chia
*   thành những interface nhỏ với nhiều mục đích cụ thể.
* D (Dependency inversion principle):
*   - Module cấp cao không nên phụ thuộc vào module cấp thấp. Cả 2 nên phụ thuộc vào
*   abstraction
*   - Interface (Abstraction) không nên phụ thuộc vào chi tiết mà ngược lại (Các
*   class giao tiếp với nhau thông qua interface, không phải thông qua implementation)
* */

@AndroidEntryPoint
class DemoCoroutineFragment
    : OthersBaseFragment<FragmentDemoCoroutineBinding>(FragmentDemoCoroutineBinding::inflate) {

    companion object {
        @JvmStatic fun newInstance() = DemoCoroutineFragment()
    }

    override fun initObserve() {

    }

    override fun initialize() {
        binding.btnIncreaseCount.setOnClickListener {
            increaseCount()
        }
        binding.btnStart.setOnClickListener {
            requestDataWithSuspend()
        }
    }

    var i = 0
    private fun increaseCount() {
        i++
        binding.tvCount.text = i.toString()
    }

    private fun requestData() {
        Log.d("DemoCoroutineFragment","Request data on ${Thread.currentThread().name}")
        Thread.sleep(8000L)
        binding.tvData.text = "Data from server"
    }

    private fun requestDataWithSuspend() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        binding.progressBar.visibility = View.VISIBLE
        binding.tvData.text = ""
        scope.launch {
            Log.d("DemoCoroutineFragment", "Request data on ${Thread.currentThread().name}")
            delay(2000L)
            binding.progressBar.visibility = View.INVISIBLE
            binding.tvData.text = "Data from server"
        }
    }
}