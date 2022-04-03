package com.example.codebaseandroidapp.viewModel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.KEY_IMAGE_URI
import com.example.codebaseandroidapp.workManager.BlurWorker
import com.example.codebaseandroidapp.workManager.CleanupWorker
import com.example.codebaseandroidapp.workManager.SaveImageToFileWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class WorkManagerViewModel @Inject constructor(@ApplicationContext mContext: Context): ViewModel() {
    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private var context = mContext
    private val workManager = WorkManager.getInstance(context)

    init {
        imageUri = getImageUri(context)
    }
    /**
     * WorkManager-2
     * WorkRequest:
     * Đại diện cho 1 yêu cầu tới Worker, có thể truyền Worker như 1 phần để khởi tạo WorkRequest,
     * khi tạo WorkRequest có thể phải xác định thêm 1 số thứ như Constraint về thời điểm Worker
     * sẽ chạy
     * Có 2 loại WorkRequest:
     * - OneTimeWorkRequest: Một WorkRequest sẽ chỉ thực thi 1 lần
     * - PeriodicWorkRequest: Một WorkRequest sẽ lặp lại theo 1 chu kỳ (Periodic: Định kỳ)
     *
     * Có thể liên kết (chain) nhiều work để thực thi tuần tự hoặc song song
     */
    internal fun applyBlur(blurLevel: Int) {

        // Add WorkRequest to Cleanup temporary images
        var continuation: WorkContinuation = workManager
            .beginWith(OneTimeWorkRequest
                .from(CleanupWorker::class.java))

        // Add WorkRequest to blur the image
        val blurRequest = OneTimeWorkRequest.Builder(BlurWorker::class.java)
            .setInputData(createInputDataForUri())
            .build()

        continuation = continuation.then(blurRequest)

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java).build()

        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    /**
     * WorkManager-3
     * Data.Builder():
     * Truyền tham số đầu vào và nhận kết quả đầu ra của Worker bằng đối tượng Data
     */
    private fun createInputDataForUri(): Data {
        val builer = Data.Builder()
        imageUri?.let {
            builer.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builer.build()
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        val imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()

        return imageUri
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }
}