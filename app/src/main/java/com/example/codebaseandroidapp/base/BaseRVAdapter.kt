package com.example.codebaseandroidapp.base

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.codebaseandroidapp.utils.Utils.Companion.checkNull

abstract class BaseRVAdapter<T : Any, VB : ViewBinding>(
    private var mContext: Context,
    config: AsyncDifferConfig<T>
) : ListAdapter<T, BaseRVAdapter<T,VB>.ItemViewHolder>(config){

    inner class ItemViewHolder(val binding: VB) : BaseViewHolder<T>(binding.root), View.OnClickListener {

        override fun onBind(item: T, position: Int) {
            super.onBind(item, position)
            bindView(this, binding, item, position)
        }

        @Suppress("DEPRECATION")
        override fun onClick(view: View?) {
            try {
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    onClickView(view, item = getItem(adapterPosition), adapterPosition)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    abstract fun bindView(holder: ItemViewHolder, binding: VB, item: T, position: Int)
    abstract fun onClickView(view: View?, item: T, position: Int)
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    override fun submitList(list: List<T>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    open fun updateList(list: List<T>?) {
        submitList(list?.let { ArrayList(it) })
    }

    open fun clearData() {
        updateList(null)
    }

    open fun addList(list: List<T>?) {
        val newListData: MutableList<T> = mutableListOf()
        newListData.addAll(currentList)
        list?.let { newListData.addAll(it) }
        submitList(newListData)
    }

    protected open fun getContext(): Context {
        return mContext
    }

    protected open fun getResources(): Resources {
        return mContext.resources
    }

    protected open fun getString(resId: Int): String {
        return mContext.getString(resId).checkNull()
    }

    protected open fun getString(resId: Int, vararg objects: Any?): String {
        return mContext.getString(resId, *objects).checkNull()
    }
}