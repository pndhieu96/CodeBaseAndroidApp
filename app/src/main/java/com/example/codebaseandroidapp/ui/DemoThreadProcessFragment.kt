package com.example.codebaseandroidapp.ui

import android.util.Log
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoThreadProcessBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

/*
* Khi một ứng dụng Android được khởi chạy, thì hệ thống Android sẽ start một Process cho ứng dụng đó
* cùng với một Thread duy nhất để thực thi. Như vậy là cả Process và Thread đều được sinh ra khi ứng dụng
* được khởi chạy, và số lượng được tạo ra là 1 Process và 1 Thread.
*
* Process: Sẽ cung cấp tài nguyên cần thiết để thực thi chương trình (Program). Mỗi Process có một không
* gian địa chỉ ảo, có các mã thực thi, có các lệnh xử lý các đối tượng hệ thống, kích thước làm việc tối
* thiểu và tối đa,... và phải có ít nhất 1 thread thực thi.
*
* Thread: Là 1 thực thể trong 1 Process, là (1 luồng) 1 đối tượng được lên kế hoạch để thực thi một nhiệm vụ
* nào đó. Có thể có nhiều thread trong 1 Process, mỗi thread lại có 1 công việc riêng của nó và sẽ chiếm 1 phần
* tài nguyên hệ thống trong Process.
* Mỗi Thread đều có độ ưu tiên riêng. Thread có thể đánh dấu là daemon. Daemon Thread là một thread có đô ưu
* tiên thấp, nó duy trì hoạt động đến khi tất cả các threads khác hoàn thành công việc hay chết đi thì nó mới
* chết theo. vd: trình dọn rác trong Java
*
* => Process = Program (chương trình cần thực thi) + all threads executing in program (tất cả các thread
* thực thi chương trình đó)
*
*
* Main Thread, UI Thread
* Trong Android khi chương trình được khởi chạy, hệ thống sẽ start một Thread ban đầu cùng với 1 Process. Thì
* Thread đó chính là Main Thread
* Trong Android Main Thread thường được gọi là UI Thread bởi vì có 2 lý do chính sau đây:
* 1. Thread này có nhiệm vụ gửi các sự kiện đến widget, tức là đến các view ở giao diện điện thoại, thậm chí cả
* các sự kiện vẽ (draw)
* 2. Ngoài ra Thread này cũng phải tương tác với bộ công cụ Android UI (Android UI Toolkit) gồm 2 gói thư viện là
* android.widget và android.view
* Main thread không là UI thread khi một chương trình có nhiều hơn 1 Thread phụ trách việc xử lý giao diện
*
* Worker Thread/ Background Thread
* Là Thread mà được tự tạo thêm cho chương trình để nó thực thi một công việc nào đó không liên quan đến giao diện
*
*
* ARN: Application not Responding
* Có 2 Rules khi làm việc với Thread:
* 1. Không được block UI Thread
* 2. Không được kết nối với bộ công cụ Android UI (Android UI toolkit) từ một Thread không phải là UI Thread
* */

@AndroidEntryPoint
class DemoThreadProcessFragment : OthersBaseFragment<FragmentDemoThreadProcessBinding>(FragmentDemoThreadProcessBinding::inflate) {

    companion object {
        @JvmStatic fun newInstance() = DemoThreadProcessFragment()
    }

    override fun initObserve() {

    }

    override fun initialize() {
        binding.btnStart.setOnClickListener {
            if(binding.lpiProcess.progress == 100) {
                reStart()
            } else {
                start()
            }
        }
    }

    private fun start() {
        Thread {
            Log.d("ThreadProcessFragment","Thread 1 requests data on ${Thread.currentThread().name}")
            startProcessInUiThread(binding.lpiProcess, 50)
        }.start()

        Thread {
            Log.d("ThreadProcessFragment","Thread 2 requests data on ${Thread.currentThread().name}")
            startProcessInUiThread(binding.lpiProcess1, 10)
        }.start()

        Thread {
            Log.d("ThreadProcessFragment","Thread 3 requests data on ${Thread.currentThread().name}")
            startProcessInUiThread(binding.lpiProcess2,30)
        }.start()
    }

    private fun startProcessInUiThread(lp: LinearProgressIndicator, delayTime: Long) {
        for(i in 0..100) {
            Thread.sleep(delayTime)
            activity?.runOnUiThread {
                lp.progress = i
            }
        }
    }

    private fun reStart() {
        binding.lpiProcess.progress = 0
        binding.lpiProcess1.progress = 0
        binding.lpiProcess2.progress = 0
    }
}