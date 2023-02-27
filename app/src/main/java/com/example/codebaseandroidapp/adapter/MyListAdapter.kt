package com.example.codebaseandroidapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.databinding.RecycleviewItemMyListBinding
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils
import javax.inject.Inject

class MyListAdapter @Inject constructor():
    ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    private var callBack: MovieListen? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var movie = getItem(position)
        (holder as ViewHolder).bind(movie)
        holder.setSearchCallBack(callBack)
    }

    fun setSearchCallBack(callBack: MovieListen) {
        this.callBack = callBack
    }

    class ViewHolder private constructor(val binding: RecycleviewItemMyListBinding) : RecyclerView.ViewHolder(binding.root) {
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
            fun from(view: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(view.context)
                val binding = RecycleviewItemMyListBinding.inflate(layoutInflater, view, false)
                return ViewHolder(binding)
            }
        }
    }
}