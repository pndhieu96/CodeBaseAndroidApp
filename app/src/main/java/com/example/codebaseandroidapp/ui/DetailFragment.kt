package com.example.codebaseandroidapp.ui

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.codebaseandroidapp.Application
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentDetailBinding
import com.example.codebaseandroidapp.utils.Utils
import com.example.codebaseandroidapp.viewModel.DetailViewModel
import com.example.codebaseandroidapp.viewModel.DetailViewModelFactory
import android.graphics.PorterDuffColorFilter
import android.widget.LinearLayout.HORIZONTAL
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.adapter.MovieListen
import com.example.codebaseandroidapp.adapter.RelativeMoviesAdapter
import com.example.codebaseandroidapp.model.LoveMovieId
import com.example.codebaseandroidapp.utils.ConstantUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel :  DetailViewModel by viewModels()
    private lateinit var navController: NavController
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        navController = findNavController(this)
        id = arguments?.getString("movieId")
        binding.button.setOnClickListener {
            navController.popBackStack()
        }
        val iconBack = resources.getDrawable(R.drawable.ic_back, null)
        binding.button.setImageDrawable(iconBack)
        binding.button.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.white),
            PorterDuff.Mode.SRC_ATOP);

        val iconHeart = resources.getDrawable(R.drawable.heart, null).mutate()
        iconHeart.setColorFilter(PorterDuffColorFilter(ContextCompat.getColor(requireContext(), android.R.color.white), PorterDuff.Mode.SRC_IN))
        iconHeart.setBounds(50, 0, 110, 60);
        binding.btnLove.setCompoundDrawables(iconHeart, null, null, null)

        binding.btnLove.setOnClickListener {
            viewModel.detailInfo.value?.let {
                if(it.id != 0) {
                    if(it.is_love) {
                        viewModel.removeFromMyList(it.id)
                    } else {
                        viewModel.addMyList(LoveMovieId(0,it.id))
                    }
                }
            }
        }

        val adapter = RelativeMoviesAdapter()
        adapter.setCallBack(MovieListen{
            val bundle = bundleOf("movieId" to it.id.toString())
            navController.navigate(R.id.action_detailFragment_self, bundle)
        })
        binding.rvRelativeMovies.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        binding.rvRelativeMovies.adapter = adapter

        viewModel.detailInfo.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.title
            binding.tvDes.text = it.overview
            binding.tvSubTitle.text =
                it.release_date + "  " + it.vote_average + "  " + it.original_language
            Glide.with(requireContext())
                .load(Utils.getImagePath(it.backdrop_path, ConstantUtils.FILE_SIZE_LANDSCAPE)).into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        binding.clTop.background = resource
                    }
                })
            if(it.is_love) {
                binding.btnLove.text = "Remove from my list"
            } else {
                binding.btnLove.text = "Love it"
            }
        }

        viewModel.relativeMovies.observe(viewLifecycleOwner) {
            adapter.submitList(it.results)
        }

        id?.let {
            viewModel.getRelativeMovie("\"$it\"")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        id?.let {
            viewModel.getDetail(it)
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
//                  Navigation-6
//                  NavController
//                  Dùng navController.PopBackStack() để back về destination trước đó trong backStack
                    navController.popBackStack()
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailFragment().apply {

            }
    }
}