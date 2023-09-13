package com.example.codebaseandroidapp.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeParentBinding
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeParentSliderBinding
import com.example.codebaseandroidapp.di.ActitvityAbstractModule
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.smarteist.autoimageslider.SliderView

import com.smarteist.autoimageslider.SliderAnimations

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

private const val ITEM_TYPE_SLIDER = 0
private const val ITEM_TYPE_LANDSCAPE = 1
private const val ITEM_TYPE_PORTRAIT = 2

class HomeParentRecycleViewAdapter @Inject constructor(
        @ActitvityAbstractModule.MovieWithGenreItemCallBack
        movieCallBack: DiffUtil.ItemCallback<MoviesWithGenre>,
        activity: Activity
    ): ListAdapter<MoviesWithGenre, RecyclerView.ViewHolder>(
        movieCallBack
    ) {

    private val hiltEntryPoint = EntryPointAccessors.fromActivity(
        activity,
        HomeParentRecycleViewAdapterEntryPoint::class.java
    )

    @InstallIn(ActivityComponent::class)
    @EntryPoint
    interface HomeParentRecycleViewAdapterEntryPoint {
        fun homeChildLandscapeRecycleViewAdapter() : HomeChildLandscapeRecycleViewAdapter
        fun homeChildPortraitRecycleViewAdapter() : HomeChildPortraitRecycleViewAdapter
    }

    private fun getHomeChildLandscapeRecycleViewAdapter(): HomeChildLandscapeRecycleViewAdapter {
        return hiltEntryPoint.homeChildLandscapeRecycleViewAdapter()
    }

    private fun getHomeChildPortraitRecycleViewAdapter(): HomeChildPortraitRecycleViewAdapter {
        return hiltEntryPoint.homeChildPortraitRecycleViewAdapter()
    }

    private var callBack: MovieListen? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            ITEM_TYPE_SLIDER -> return SliderViewHolder.from(parent)
            ITEM_TYPE_LANDSCAPE -> return LanscapeViewHolder.from(parent, getHomeChildLandscapeRecycleViewAdapter())
            ITEM_TYPE_PORTRAIT -> return PortraitViewHolder.from(parent, getHomeChildPortraitRecycleViewAdapter())
            else -> return PortraitViewHolder.from(parent, getHomeChildPortraitRecycleViewAdapter())
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder) {
            is PortraitViewHolder -> {
                holder.bind(item)
                callBack?.let {
                    holder.setCallBack(it)
                }
            }
            is LanscapeViewHolder -> {
                holder.bind(item)
                callBack?.let {
                    holder.setCallBack(it)
                }
            }
            is SliderViewHolder -> {
                holder.bind(item)
                callBack?.let {
                    holder.setCallBack(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) {
            ITEM_TYPE_SLIDER
        } else if(position%3==0) {
            ITEM_TYPE_LANDSCAPE
        } else {
            ITEM_TYPE_PORTRAIT
        }
    }

    fun setCallBack(mCallback: MovieListen) {
        this.callBack = mCallback
    }

    class LanscapeViewHolder private constructor(
        val binding: RecycleviewItemHomeParentBinding,
        val adapter: HomeChildLandscapeRecycleViewAdapter
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(moviesWithGenre: MoviesWithGenre) {
            binding.title.text = moviesWithGenre.name
            binding.recycleViewChild.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recycleViewChild.adapter = adapter
            adapter.submitList(moviesWithGenre.movies)
        }

        fun setCallBack(mCallback: MovieListen) {
            adapter.setCallBack(mCallback)
        }

        companion object {
            fun from(parent: ViewGroup, adapter: HomeChildLandscapeRecycleViewAdapter): LanscapeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemHomeParentBinding.inflate(layoutInflater, parent, false)
                return LanscapeViewHolder(binding, adapter)
            }
        }
    }

    class PortraitViewHolder private constructor(
        val binding: RecycleviewItemHomeParentBinding,
        val adapter: HomeChildPortraitRecycleViewAdapter
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(moviesWithGenre: MoviesWithGenre) {
            binding.title.text = moviesWithGenre.name
            binding.recycleViewChild.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recycleViewChild.adapter = adapter
            adapter.submitList(moviesWithGenre.movies)
        }

        fun setCallBack(mCallback: MovieListen) {
            adapter.setCallBack(mCallback)
        }

        companion object {
            fun from(parent: ViewGroup, adapter: HomeChildPortraitRecycleViewAdapter): PortraitViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemHomeParentBinding.inflate(layoutInflater, parent, false)
                return PortraitViewHolder(binding, adapter)
            }
        }
    }

    class SliderViewHolder private constructor(val binding: RecycleviewItemHomeParentSliderBinding): RecyclerView.ViewHolder(binding.root) {
        val adapter =
            HomChildSliderAdapter(binding.root.context)

        fun bind(moviesWithGenre: MoviesWithGenre) {
            adapter.renewItems(moviesWithGenre.movies.subList(0,3))
            binding.imageSlider.setSliderAdapter(adapter)

            binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

            binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            binding.imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            binding.imageSlider.indicatorSelectedColor = Color.WHITE
            binding.imageSlider.indicatorUnselectedColor = Color.GRAY
            binding.imageSlider.scrollTimeInSec = 4 //set scroll delay in seconds :

            binding.imageSlider.startAutoCycle()
        }

        fun setCallBack(mCallback: MovieListen) {
            adapter.setCallBack(mCallback)
        }

        companion object {
            fun from(parent: ViewGroup): SliderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemHomeParentSliderBinding.inflate(layoutInflater, parent, false)
                return SliderViewHolder(binding)
            }
        }
    }
}