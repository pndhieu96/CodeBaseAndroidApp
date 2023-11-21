package com.example.codebaseandroidapp.ui

import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoCoroutineBinding
import com.google.firebase.components.ComponentRuntime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/*
* Coroutine (suspend, pause, resume): Thường được sử dụng để xử lý các tác vụ bất đồng bộ mà không làm block main thread (như
*   là gọi API, đọc và ghi tệp tin, thao tác với cơ ở dữ liệu) thay thế cho các cách truyền thống như sử dụng AsyncTask
*   hoặc luồng (thread)
*
* - Suspend function: là 1 hàm để làm việc với coroutine và được đánh dấu bằng từ khoá suspend có khả năng pause thực
*       thi và resume từ nơi đã tạm dừng.
*   - khi 1 hàm suspend được gọi, nó có thể tạm dừng thực thi mà không làm đóng băng luồng (Thread), cho phép luồng chình
*       (hay coroutine khác) tiếp tục thực thi công việc khác khi đang chờ hàm suspend hoàn thành
*
* - Coroutine cotext: (gồm 4 thành phần chính)
*   1. Dispatcher: Để thiết lập coroutine chạy trên thread nào gồm các thread
*       - IO, Dùng cho các nhiệm vụ nhập xuất dữ liệu như gọi api
*       - Default, Để dùng cho việc xử lý dữ liệu mất nhiều thời gian (Như làm việc với một mảng danh sách dữ liệu lớn)
*       - Main, Để dùng cho tương tác và hiển thị cho người dùng
*       - Unconfined: Coroutine scope khai báo ở Dispatcher Unconfined thì khi dùng withcontext để xử lý ở thread
*           khác thì sau khi xử lý xong thread ở coroutine scope sẽ resume ở dispatcher của withcontext
*       - WithContext: Dùng để chuyển qua lại giữa các dispatcher trong 1 coroutine scope
*   2. Job: Để quản lý vòng đời của coroutine
*       - Job.invokeOnCompletion: Để theo dõi job đã chạy xong chưa, thành công hay có lỗi
*       - Job lifecycle: gồm các trạng thái chính New -> Active -> Completed hoặc Cancelled (Khi bị cancel hay lỗi)
*           - Completed: isCompleted = true, isCancelled = false
*           - Cancelled: isCompleted = true, isCancelled = true
*       - Cancellation:
*           - Có thể cancel 1 coroutine bằng cách gọi job.cancel() hoặc coroutineScope.cancel()
*           - Phải sử dụng ensureActive() hoặc isActive để kiểm tra xem trạng thái của job là đang active trước khi
*               bắt đầu chạy bất kỳ công việc tốn nhiều thời gian
*           - Nếu sử dụng suspend funtion như là withContext hay delay(),... từ kotlin.coroutine thì những function này
*               sẽ tự kiểm tra trạng thái active của job
*       - Supervisor job:
*           - Con của supervisor job có thể fail độc lập với nhau
*   3. Name: Tên của coroutine
*   4. CoroutineExceptionHandler: Quản lý các ngoại lệ
*       - Nếu khi xử lý coroutine throw exception là CancellationException thì đó sẽ được tính là sự huỷ bỏ và job
*           của coroutine sẽ có trạng thái là cancelled
*       - Nếu khi xử lý coroutine throw exception không là CancelationException thì đó sẻ được tính là lỗi và nếu không
*           xử lý lỗi thì app sẽ bị crash vì thế cần dùng CoroutineExceptionHandler để xử lý lỗi
*
* - Cách khởi tạo coroutine:
*   - launch: trả về 1 job để quản lý vòng đời của coroutine
*   - async: trả vê 1 deferred kế thừa từ job nhưng chứa thêm giá trị trả về từ coroutine và có thể lấy ra bằng
*       cách gọi deferred.await
*
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
//            demoLauchAndAsync()
//            demoInvokeOnCompletionAndLifeCycle()
//            demoCoroutineExceptionHandler()
//            demoCancellation()
//            demoSupervisorJob()
        }
        binding.btnCoroutineScope.setOnClickListener {
            navController.navigate(R.id.action_demoCoroutineFragment_to_demoCoroutineScopeFragment)
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
                delay(6000L)
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
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("DemoCoroutineFragment", "CoroutineExceptionHandler coroutineContext = $coroutineContext")
            Log.d("DemoCoroutineFragment", "CoroutineExceptionHandler throwable = $throwable")
        }
        val mainScope = CoroutineScope(Job() + Dispatchers.Main + handler)
        val parentJob = mainScope.launch {
            val job1 = launch {
                getHomeBanner(1)
                Log.d("DemoCoroutineFragment", "Job1 finish")
            }
            job1.invokeOnCompletion {
                it?.let {
                    Log.d("DemoCoroutineFragment", "Job1 throwable = $it")
                }
                Log.d("DemoCoroutineFragment", "Job1 $job1")
            }

            val job2 = launch {
                getHomeBanner(2)
                Log.d("DemoCoroutineFragment", "Job2 finish")
            }
            job2.invokeOnCompletion {
                it?.let {
                    Log.d("DemoCoroutineFragment", "Job2 throwable = $it")
                }
                Log.d("DemoCoroutineFragment", "Job2 $job2")
            }
        }
        parentJob.invokeOnCompletion {
            it?.let {
                Log.d("DemoCoroutineFragment", "parentJob throwable = $it")
            }
            Log.d("DemoCoroutineFragment", "parentJob $parentJob")
        }
    }

    private fun demoCancellation() {
        val mainScope = CoroutineScope(Dispatchers.Main)
        mainScope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.IO) {
                var nextPrintTime = startTime
                i = 0
                while (i < 5) {
                    ensureActive()
                    if(System.currentTimeMillis() >= nextPrintTime) {
                        Log.d("DemoCoroutineFragment", "Hello ${i++}")
                        nextPrintTime = System.currentTimeMillis() + 500L
                    }
                }
            }
            delay(1000L)
            Log.d("DemoCoroutineFragment", "Cancel")
            job.cancel()
            Log.d("DemoCoroutineFragment", "Cancelled")
        }
    }

    private fun demoSupervisorJob() {
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("DemoCoroutineFragment", "CoroutineExceptionHandler coroutineContext = $coroutineContext")
            Log.d("DemoCoroutineFragment", "CoroutineExceptionHandler throwable = $throwable")
        }
        val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + handler)
        val job1 = mainScope.launch {
            getHomeBanner(1)
            Log.d("DemoCoroutineFragment", "Job2 finish")
        }
        val job2 = mainScope.launch {
            getHomeBanner(2)
            Log.d("DemoCoroutineFragment", "Job2 finish")
        }
        job1.invokeOnCompletion {
            Log.d("DemoCoroutineFragment", "job1 $job1")
        }
        job2.invokeOnCompletion {
            Log.d("DemoCoroutineFragment", "job2 $job2")
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