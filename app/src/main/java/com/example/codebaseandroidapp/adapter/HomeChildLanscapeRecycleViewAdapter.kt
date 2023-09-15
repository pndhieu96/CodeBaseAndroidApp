package com.example.codebaseandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.base.BaseRVAdapter
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeChildLanscapeBinding
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils

class HomeChildLandscapeRecycleViewAdapter
    constructor(
        mContext: Context,
        config: AsyncDifferConfig<Movie>
    )
    : BaseRVAdapter<Movie, RecycleviewItemHomeChildLanscapeBinding>(mContext, config) {
    private var callBack: MovieListen? = null

    fun setCallBack(mCallback: MovieListen) {
        this.callBack = mCallback;
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> RecycleviewItemHomeChildLanscapeBinding
        get() = RecycleviewItemHomeChildLanscapeBinding::inflate

    override fun bindView(
        holder: ItemViewHolder,
        binding: RecycleviewItemHomeChildLanscapeBinding,
        item: Movie,
        position: Int
    ) {
        Glide.with(binding.root.context)
            .load(Utils.getImagePath(item.backdrop_path, ConstantUtils.FILE_SIZE_LANDSCAPE))
            .into(binding.ivMovie)
    }

    override fun onClickView(view: View?, item: Movie, position: Int) {
        callBack?.onCLick(item)
    }
}