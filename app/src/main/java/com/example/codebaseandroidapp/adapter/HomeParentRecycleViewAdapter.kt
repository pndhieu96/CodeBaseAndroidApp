package com.example.codebaseandroidapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeParentBinding
import com.example.codebaseandroidapp.databinding.RecycleviewItemHomeParentSliderBinding
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.utils.Utils
import com.smarteist.autoimageslider.SliderView

import com.smarteist.autoimageslider.SliderAnimations

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import javax.inject.Inject
import javax.inject.Singleton

private const val ITEM_TYPE_SLIDER = 0
private const val ITEM_TYPE_LANSCAPE = 1
private const val ITEM_TYPE_PORTRAIT = 2

@Singleton
class HomeParentRecycleViewAdapter @Inject constructor():
    ListAdapter<MoviesWithGenre, RecyclerView.ViewHolder>(MoviesWithGenreDiffCallback()) {

    private var callBack: MovieListen? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            ITEM_TYPE_SLIDER -> return SliderViewHolder.from(parent)
            ITEM_TYPE_LANSCAPE -> return LanscapeViewHolder.from(parent)
            ITEM_TYPE_PORTRAIT -> return PortraitViewHolder.from(parent)
            else -> return PortraitViewHolder.from(parent)
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
        if(position == 0) {
            return ITEM_TYPE_SLIDER
        } else if(position%3==0) {
            return ITEM_TYPE_LANSCAPE
        } else {
            return ITEM_TYPE_PORTRAIT
        }
    }

    fun setCallBack(mCallback: MovieListen) {
        this.callBack = mCallback;
    }

    class LanscapeViewHolder private constructor(val binding: RecycleviewItemHomeParentBinding): RecyclerView.ViewHolder(binding.root) {
        val adapter = HomeChildLanscapeRecycleViewAdapter()

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
            fun from(parent: ViewGroup): LanscapeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemHomeParentBinding.inflate(layoutInflater, parent, false)
                return LanscapeViewHolder(binding)
            }
        }
    }

    class PortraitViewHolder private constructor(val binding: RecycleviewItemHomeParentBinding): RecyclerView.ViewHolder(binding.root) {
        val adapter = HomeChildPortraitRecycleViewAdapter()

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
            fun from(parent: ViewGroup): PortraitViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecycleviewItemHomeParentBinding.inflate(layoutInflater, parent, false)
                return PortraitViewHolder(binding)
            }
        }
    }

    class SliderViewHolder private constructor(val binding: RecycleviewItemHomeParentSliderBinding): RecyclerView.ViewHolder(binding.root) {
        val adapter = SliderAdapterExample(binding.root.context)

        fun bind(moviesWithGenre: MoviesWithGenre) {
            adapter.renewItems(moviesWithGenre.movies.subList(0,3))
            binding.imageSlider.setSliderAdapter(adapter)

            binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

            binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
            binding.imageSlider.setIndicatorSelectedColor(Color.WHITE)
            binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY)
            binding.imageSlider.setScrollTimeInSec(4) //set scroll delay in seconds :

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