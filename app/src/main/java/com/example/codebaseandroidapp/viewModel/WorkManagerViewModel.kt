package com.example.codebaseandroidapp.viewModel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.codebaseandroidapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class WorkManagerViewModel @Inject constructor(@ApplicationContext context: Context): ViewModel() {
    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    init {
        imageUri = getImageUri(context)
    }
    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {

    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
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