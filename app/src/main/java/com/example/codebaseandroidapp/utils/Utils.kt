package com.example.codebaseandroidapp.utils

import android.app.*
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.codebaseandroidapp.model.ApiError
import com.example.codebaseandroidapp.base.Resource
import com.example.codebaseandroidapp.model.ResourceStatus
import com.example.codebaseandroidapp.viewModel.NetworkUtils
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.concurrent.TimeUnit


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

        fun String?.checkNull(): String {
            return if (this.isNullOrEmpty()) "" else this
        }

        fun getTimeStamp(): String {
            return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
        }

        fun getJsonParser(): Json {
            return Json {
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
                ignoreUnknownKeys = true
            }
        }

        fun Exception.printLog() {
            try {
                Timber.tag("AppException").e(this.message.checkNull())
            } catch (_: Exception) {
            }
        }

        fun Context.checkNetwork(online: () -> Unit, disconnect: (() -> Unit)? = null) {
            if (NetworkUtils.isNetworkConnected()) {
                online.invoke()
            } else {
                if (disconnect != null) {
                    disconnect.invoke()
                } else {
                    toast("Không có kết nối")
                }
            }
        }

        fun Context.toast(message: String, isLong: Boolean = false) {
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
            }
        }

        inline fun <R, A> ifIsNotNull(a: A?, isNotNull: (A) -> R, isNull: () -> R) =
            if (a != null) {
                isNotNull(a)
            } else isNull()

        /**
         * Performs [R] when [T] is not null. Block [R] will have context of [T]
         */
        inline fun <T : Any, R> ifNotNull(input: T?, callback: (T) -> R): R? {
            return input?.let(callback)
        }
    }
}