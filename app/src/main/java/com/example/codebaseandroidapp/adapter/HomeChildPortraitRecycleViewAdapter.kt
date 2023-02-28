package com.example.codebaseandroidapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeChildBinding
import com.example.codebaseandroidapp.di.ActitvityAbstractModule
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils.Companion.getImagePath
import javax.inject.Inject

private const val ITEM_TYPE_PORTRAIT = 1
class HomeChildPortraitRecycleViewAdapter
    @Inject constructor(
        @ActitvityAbstractModule.MovieItemCallBack
        movieCallBack: DiffUtil.ItemCallback<Movie>
    ): ListAdapter<Movie, RecyclerView.ViewHolder>(
        movieCallBack
    ) {
    private var callBack: MovieListen? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            ITEM_TYPE_PORTRAIT -> return PortraitViewHolder.from(parent)
        }
        return PortraitViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder) {
            is PortraitViewHolder -> {
                holder.bind(item)
                holder.getCardView().setOnClickListener {
                    callBack?.onCLick(item)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  ITEM_TYPE_PORTRAIT
    }

    fun setCallBack(mCallback: MovieListen) {
        this.callBack = mCallback;
    }

    class PortraitViewHolder private constructor(val binding: RecycleviewItemHomeChildBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie) {
            Glide.with(binding.root.context)
                .load(getImagePath(movie.poster_path ,ConstantUtils.FILE_SIZE_PORTRAIT))
                .into(binding.ivMovie)
        }

        fun getCardView(): CardView {
            return binding.cardView
        }

        companion object {
            fun from(parent: ViewGroup): PortraitViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemHomeChildBinding.inflate(layoutInflater, parent, false)
                return PortraitViewHolder(binding)
            }
        }
    }
}