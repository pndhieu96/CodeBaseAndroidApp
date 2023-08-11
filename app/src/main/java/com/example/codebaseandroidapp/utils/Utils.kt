package com.example.codebaseandroidapp.utils

import android.app.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.codebaseandroidapp.model.ApiError
import com.example.codebaseandroidapp.model.Resource
import com.example.codebaseandroidapp.model.ResourceStatus


class Utils {
    companion object {
        fun getOriginImagePath(path: String?): String {
            path?.let {
                return "https://image.tmdb.org/t/p/original$it"
            }
            return ""
        }

        fun getImagePath(path: String?, fileSize: String): String {
            path?.let {
                return "https://image.tmdb.org/t/p/$fileSize$it"
            }
            return ""
        }

        fun hideKeyboard(activity: Activity) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view: View? = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        fun showKeyBoard(activity: Activity, view: EditText) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }

        fun <T> LiveData<Resource<T>>.observer(
            owner: LifecycleOwner,
            onSuccess: ((T) -> Unit)? = null,
            onError: ((ApiError) -> Unit)? = null,
            onLoading: (() -> Unit)? = null
        ) {
            observe(owner) {
                when (it.status) {
                    ResourceStatus.SUCCESS -> it.getContentIfNotHandled()?.let { data ->
                        onSuccess?.invoke(data)
                    }
                    ResourceStatus.ERROR -> it.getErrorIfNotHandled()?.let { error ->
                        onError?.invoke(error)
                    }
                    ResourceStatus.LOADING -> it.isLoadingIfNotHandled()?.let { loading ->
                        onLoading?.invoke()
                    }
                }
            }
        }
    }
}