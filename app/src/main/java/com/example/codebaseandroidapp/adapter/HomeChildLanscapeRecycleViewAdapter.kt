package com.example.codebaseandroidapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeChildBinding
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeChildLanscapeBinding
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils.Companion.getImagePath

private const val ITEM_TYPE_LANCAPE = 0
class HomeChildLanscapeRecycleViewAdapter : ListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    private var callBack: MovieListen? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            ITEM_TYPE_LANCAPE -> return LanscapeViewHolder.from(parent)
        }
        return LanscapeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder) {
            is LanscapeViewHolder ->  {
                holder.bind(item)
                holder.getCardView().setOnClickListener {
                    callBack?.onCLick(item)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_TYPE_LANCAPE
    }

    fun setCallBack(mCallback: MovieListen) {
        this.callBack = mCallback;
    }

    class LanscapeViewHolder private constructor(val binding: RecycleviewItemHomeChildLanscapeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie) {
            movie?.let {
                Glide.with(binding.root.context)
                    .load(getImagePath(movie.backdrop_path ?: "",ConstantUtils.FILE_SIZE_LANDSCAPE))
                    .into(binding.ivMovie)
            }
        }

        fun getCardView() : CardView {
            return binding.cardView
        }

        companion object {
            fun from(parent: ViewGroup): LanscapeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemHomeChildLanscapeBinding.inflate(layoutInflater, parent, false)
                return LanscapeViewHolder(binding)
            }
        }
    }
}