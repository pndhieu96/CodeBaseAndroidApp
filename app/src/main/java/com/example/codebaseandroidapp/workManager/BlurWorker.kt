package com.example.codebaseandroidapp.workManager

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.KEY_IMAGE_URI
import com.example.codebaseandroidapp.utils.Utils.Companion.blurBitmap
import com.example.codebaseandroidapp.utils.Utils.Companion.makeStatusNotification
import com.example.codebaseandroidapp.utils.Utils.Companion.sleep
import com.example.codebaseandroidapp.utils.Utils.Companion.writeBitmapToFile

/**
 * WorkManager-1
 * Đểu thực hiện các công việc trong Background
 * (Opportunistic execution) Nó thực hiện công việc ngay khi có thể
 * (Guaranteed execution) Nó quan tâm đến logic để bắt đầu công việc
    * trong nhiều tình huống khác nhau, ngay cả khi bạn điều hướng ra khỏi ứng dụng
 * Nó có 3 thành phần chính:
    * Work: Nơi công việc thực sự được thực thi
    * WorkRequest: Đại diện cho yêu cầu thực thi 1 số công việc cụ thể
    * WorkManager: Lớp thực sự schedules (lập lịch) cho WorkRequest và khiến nó chạy
 * */

 /*
 * Worker:
 * Nơi đặt code cho một công việc mà bạn muốn thực thi trong backgroud
 * Extend Worker class and override doWork() Method (chức năng)
 * */
private const val TAG = "BlurWorker"
class BlurWorker(context: Context, params: WorkerParameters) : Worker(context, params){
    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)

        // ADD THIS TO SLOW DOWN THE WORKER
        sleep()
        // ^^^^

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.failure()
        }
    }

}