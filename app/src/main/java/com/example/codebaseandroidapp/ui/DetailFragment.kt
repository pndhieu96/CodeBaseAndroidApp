package com.example.codebaseandroidapp.ui

import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentDetailBinding
import com.example.codebaseandroidapp.utils.Utils
import com.example.codebaseandroidapp.viewModel.DetailViewModel
import android.graphics.PorterDuffColorFilter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.adapter.RelativeMoviesAdapter
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.model.LoveMovieId
import com.example.codebaseandroidapp.network.DetailPostAPI
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils.Companion.checkNetwork
import com.example.codebaseandroidapp.utils.Utils.Companion.observer
import com.example.codebaseandroidapp.viewModel.GetDetailViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private var id: String? = null
    private val viewModel: DetailViewModel by viewModel()
    private val getDetailViewModel: GetDetailViewModel by viewModel()
    private val adapter: RelativeMoviesAdapter by inject()

    override fun initObserve() {
        viewModel.relativeMovies.observer(viewLifecycleOwner,
            onSuccess = {data->
                adapter.submitList(data.results)
            }, onError = {error->
                Toast.makeText(context, error.statusMessage, Toast.LENGTH_SHORT).show()
            })

        getDetailViewModel.observe(viewLifecycleOwner,
            onShowLoading = {

            },
            onSuccess = { it, _, _ ->
                binding.tvTitle.text = it.title
                binding.tvDes.text = it.overview
                binding.tvSubTitle.text =
                    it.release_date + "  " + it.vote_average + "  " + it.original_language
                Glide.with(requireContext())
                    .load(Utils.getImagePath(it.backdrop_path, ConstantUtils.FILE_SIZE_LANDSCAPE))
                    .into(binding.ivBgTop)
                if (it.is_love) {
                    binding.btnLove.text = "Remove from my list"
                } else {
                    binding.btnLove.text = "Love it"
                }
            })
    }

    override fun initialize() {
        id = arguments?.getString("movieId")
        binding.button.setOnClickListener {
            navController.popBackStack()
        }
        val iconBack = resources.getDrawable(R.drawable.ic_back, null)
        binding.button.setImageDrawable(iconBack)
        binding.button.setColorFilter(
            ContextCompat.getColor(requireContext(), android.R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

        val iconHeart = resources.getDrawable(R.drawable.heart, null).mutate()
        iconHeart.setColorFilter(
            PorterDuffColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                ), PorterDuff.Mode.SRC_IN
            )
        )
        iconHeart.setBounds(50, 0, 110, 60)
        binding.btnLove.setCompoundDrawables(iconHeart, null, null, null)

        binding.btnLove.setOnClickListener {
            viewModel.detailInfo.value?.let {
                if (it.data?.id != 0) {
                    if (it.data?.is_love == true) {
                        viewModel.removeFromMyList(it.data.id)
                    } else {
                        it.data?.let { detail ->
                            viewModel.addMyList(LoveMovieId(0, detail.id))
                        }
                    }
                }
            }
        }

        adapter.setCallBack(MovieListen {
            val bundle = bundleOf("movieId" to it.id.toString())
            navController.navigate(R.id.action_detailFragment_self, bundle)
        })
        binding.rvRelativeMovies.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRelativeMovies.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        id?.let {
            callApiGetData(it)
            viewModel.getRelativeMovie("\"$it\"")
        }

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    navController.popBackStack()
                }
            }
        )
    }

    private fun callApiGetData(id: String) {
        requireContext().checkNetwork(
            online = {
                getDetailViewModel.callAPI(dataPostAPI = DetailPostAPI(id))
            },
            disconnect = {

            })
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailFragment()
    }
}