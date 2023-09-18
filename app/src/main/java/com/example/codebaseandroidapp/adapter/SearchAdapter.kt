package com.example.codebaseandroidapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.callBack.MovieDiffCallback
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.databinding.RecycleviewItemSearchBinding
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils
import javax.inject.Inject

class SearchAdapter():
    PagingDataAdapter<Movie, SearchAdapter.ViewHolder>(MovieDiffCallback()) {

    private var callBack: MovieListen? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
        holder.setSearchCallBack(callBack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun setSearchCallBack(callBack: MovieListen) {
        this.callBack = callBack
    }

    class ViewHolder private constructor(val binding: RecycleviewItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        private var callBack: MovieListen? = null

        fun bind(item: Movie) {
            item.let {
                Glide.with(binding.root.context)
                    .load(Utils.getImagePath(it.poster_path, ConstantUtils.FILE_SIZE_PORTRAIT))
                    .into(binding.ivMovie)

                binding.cardView.setOnClickListener { _ ->
                    callBack?.onCLick(it)
                }
            }
        }

        fun setSearchCallBack(callBack: MovieListen?) {
            this.callBack = callBack
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemSearchBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
