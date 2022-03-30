package com.example.codebaseandroidapp.utils

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService








class Utils {
    companion object {
        fun getOriginImagePath(path: String): String {
            path?.let {
                return "https://image.tmdb.org/t/p/original$it"
            }
            return ""
        }

        fun getImagePath(path: String, fileSize: String): String {
            var url = ""
            path?.let {
                url = "https://image.tmdb.org/t/p/$fileSize$it"
            }
            Log.d("getImagePath", url)
            return url
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
    }
}