package com.example.codebaseandroidapp.ui

import android.util.Log
import android.view.View
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/*
* Coroutine (suspend, resume)
*
* Coroutine cotext: gồm
*   - Dispatcher: Để thiết lập coroutine chạy trên thread nào gồm các thread
*           - IO, Dùng cho các nhiệm vụ nhập xuất dữ liệu như gọi api
*           - Default, Để dùng cho việc xử lý dữ liệu mất nhiều thời gian (Như làm việc với
*           một mảng danh sách dữ liệu lớn)
*           - Main, Để dùng cho tương tác và hiển thị cho người dùng
*           - Unconfined, Coroutine scope khai báo ở Dispatcher Unconfined thì khi dùng withcontext
*               để xử lý ở thread khác thì sau khi xử lý xong thread ở coroutine scope sẽ resume
*               ở dispatcher của withcontext
*       - WithContext: Dùng withContext để chuyển qua lại giữa các dispatcher trong 1 coroutine scope
*   - Job: Để quản lý vòng đời của coroutine
*       - Job.invokeOnCompletion: Để theo dõi job đã chạy xong chưa, thành công hay có lỗi
*       - Job lifecycle: gồm các trạng thái chính New -> Active -> Completed hoặc Cancelled (Khi bị cancel hay lỗi)
*           - Completed: isCompleted = true, isCancelled = false
*           - Cancelled: isCompleted = true, isCancelled = true
*   - Name: Tên của coroutine
*   - CoroutineExceptionHandler: Quản lý các ngoại lệ
*       - Nếu khi xử lý coroutine throw exception là CancellationException thì đó sẽ được tính là sự huỷ bỏ
*           và job của coroutine sẽ có trạng thái là cancelled
*       - Nếu khi xử lý coroutine throw exception không là CancelationException thì đó sẻ được tính là lỗi
*           và nếu không xử lý lỗi thì app sẽ bị crash vì thế cần dùng CoroutineExceptionHandler để xử lý lỗi
*
* - Cách khởi tạo coroutine:
*   - launch: trả về 1 job để quản lý vòng đời của coroutine
*   - async: trả vê 1 deferred kế thừa từ job nhưng chứa thêm giá trị trả về từ
*       coroutine và có thể lấy ra bằng cách gọi deferred.await
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
//            requestDataWithSuspend()
//            demoLauchAndAsync()
//            demoInvokeOnCompletionAndLifeCycle()
            demoCoroutineExceptionHandler()
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
        val mainScope = CoroutineScope(Dispatchers.Main)
        mainScope.launch {
            Log.d("DemoCoroutineFragment", "Request data on ${Thread.currentThread().name}")
            binding.progressBar.visibility = View.VISIBLE
            binding.tvData.text = ""

            withContext(Dispatchers.IO) {
                Log.d("DemoCoroutineFragment", "Request data on ${Thread.currentThread().name}")
                delay(2000L)
            }

            Log.d("DemoCoroutineFragment", "Request data on ${Thread.currentThread().name}")
            binding.progressBar.visibility = View.INVISIBLE
            binding.tvData.text = "Data from server"
        }
    }

    private fun demoLauchAndAsync() {
        val mainScope = CoroutineScope(Dispatchers.Main)
        binding.progressBar.visibility = View.VISIBLE
        binding.tvData.text = ""
        val parentJob = mainScope.launch {
            Log.d("DemoCoroutineFragment", "MainScope request data on ${Thread.currentThread().name}")
            val job1 = launch {
                Log.d("DemoCoroutineFragment", "Launch request data on ${Thread.currentThread().name}")
                delay(1000L)
                "result from launch"
                Log.d("DemoCoroutineFragment", "job1 finish")
            }

            val deferred = async {
                Log.d("DemoCoroutineFragment", "Deferred request data on ${Thread.currentThread().name}")
                delay(2000L)
                Log.d("DemoCoroutineFragment", "deferred finish")
                "result from async"
            }

            val result = deferred.await()
            Log.d("DemoCoroutineFragment", "MainScope request data on ${Thread.currentThread().name}")
            binding.progressBar.visibility = View.INVISIBLE
            binding.tvData.text = result
        }
    }

    private fun demoInvokeOnCompletionAndLifeCycle() {
        val mainScope = CoroutineScope(Dispatchers.Main)
        val parentJob = mainScope.launch {
            val job1 = launch {
                delay(1000L)
            }
            Log.d("DemoCoroutineFragment", "Job 1 $job1")

            val job2 = launch(start = CoroutineStart.LAZY) {
                delay(3000L)
            }
            Log.d("DemoCoroutineFragment", "Job 2 $job2")
            job2.start()
            Log.d("DemoCoroutineFragment", "Job 2 $job2")
            job2.invokeOnCompletion {
                Log.d("DemoCoroutineFragment", "Job 2 $job2")
            }
        }

        parentJob.invokeOnCompletion { handler ->
            if(handler == null) {
                Log.d("DemoCoroutineFragment", "Parent job finishes success on ${Thread.currentThread().name}")
            } else {
                Log.d("DemoCoroutineFragment", "Parent job finishes fail with $handler on ${Thread.currentThread().name}")
            }
        }
    }

    private fun demoCoroutineExceptionHandler() {
        val mainScope = CoroutineScope(Job() + Dispatchers.Main)
        val parentJob = mainScope.launch {
            val job1 = launch {
                getHomeBanner(1)
                Log.d("DemoCoroutineFragment", "Job1 finish")
            }
            job1.invokeOnCompletion {
                Log.d("DemoCoroutineFragment", "Job1 $job1")
            }

            val job2 = launch {
                getHomeBanner(2)
                Log.d("DemoCoroutineFragment", "Job2 finish")
            }
        }
        parentJob.invokeOnCompletion {
            if(it == null) {
                Log.d("DemoCoroutineFragment", "Parent job finishes success on ${Thread.currentThread().name}")
            } else {
                Log.d("DemoCoroutineFragment", "Parent job finishes fail with $it on ${Thread.currentThread().name}")
            }
        }
    }

    private suspend fun getHomeBanner(index: Int) {
        val delayTime = index * 1000L
        delay(delayTime)
        when(index) {
            0 -> throw CancellationException("coroutine is cancelled")
            1 -> throw Exception("coroutine is faild")
            2 -> Log.d("DemoCoroutineFragment", "HomeBanner for index $index")
        }
    }
}