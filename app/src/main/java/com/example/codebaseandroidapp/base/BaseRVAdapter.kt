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

    abstract fun bindView(holder: ItemViewHolder, binding: VB, item: T, position: Int)
    abstract fun onClickView(view: View?, item: T, position: Int)
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    inner class ItemViewHolder(val binding: VB) : BaseViewHolder<T>(binding.root){

        override fun onBind(mItem: T, position: Int) {
            super.onBind(mItem, position)
            bindView(this, binding, mItem, position)
            binding.root.setOnClickListener {
                try {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onClickView(it, item = getItem(adapterPosition), adapterPosition)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

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

    protected fun getContext(): Context {
        return mContext
    }

    protected fun getResources(): Resources {
        return mContext.resources
    }

    protected fun getString(resId: Int): String {
        return mContext.getString(resId).checkNull()
    }

    protected fun getString(resId: Int, vararg objects: Any?): String {
        return mContext.getString(resId, *objects).checkNull()
    }
}