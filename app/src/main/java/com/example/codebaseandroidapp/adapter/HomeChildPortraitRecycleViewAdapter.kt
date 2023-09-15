package com.example.codebaseandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.base.BaseRVAdapter
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeChildBinding
import com.example.codebaseandroidapp.di.ActitvityAbstractModule
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils.Companion.getImagePath
import javax.inject.Inject

class HomeChildPortraitRecycleViewAdapter
    constructor(
        mContext: Context,
        config: AsyncDifferConfig<Movie>
    ): BaseRVAdapter<Movie, RecycleviewItemHomeChildBinding>(mContext, config) {
    private var callBack: MovieListen? = null

    fun setCallBack(mCallback: MovieListen) {
        this.callBack = mCallback;
    }

    override fun bindView(
        holder: ItemViewHolder,
        binding: RecycleviewItemHomeChildBinding,
        item: Movie,
        position: Int
    ) {
        Glide.with(binding.root.context)
            .load(getImagePath(item.poster_path ,ConstantUtils.FILE_SIZE_PORTRAIT))
            .into(binding.ivMovie)
    }

    override fun onClickView(view: View?, item: Movie, position: Int) {
        callBack?.onCLick(item)
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> RecycleviewItemHomeChildBinding
        get() = RecycleviewItemHomeChildBinding::inflate
}