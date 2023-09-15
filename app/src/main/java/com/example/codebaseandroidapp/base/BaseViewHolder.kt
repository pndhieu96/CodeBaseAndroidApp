package com.example.codebaseandroidapp.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<T : Any>(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        inline fun <reified V : ViewBinding> ViewGroup.toBinding() : V {
            return V::class.java.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).invoke(null, LayoutInflater.from(context), this, false) as V
        }
    }

    open fun onBind(Item: T, position: Int) {}
}