package com.example.codebaseandroidapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.databinding.RecycleviewItemRelativeMoviesBinding
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils

class RelativeMoviesAdapter : ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    private var callBack: MovieListen? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
        holder.setCallBack(callBack)
    }

    fun setCallBack(_callBack: MovieListen) {
        callBack = _callBack
    }

    class ViewHolder private constructor(val binding: RecycleviewItemRelativeMoviesBinding): RecyclerView.ViewHolder(binding.root) {
        private var callBack: MovieListen? = null

        fun bind(item: Movie) {
            Glide.with(binding.root.context)
                .load(Utils.getImagePath(item.poster_path, ConstantUtils.FILE_SIZE_PORTRAIT))
                .into(binding.ivMovie)
            binding.cardView.setOnClickListener {
                callBack?.onCLick(item)
            }
        }

        fun setCallBack(_callBack: MovieListen?) {
            callBack = _callBack
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemRelativeMoviesBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}