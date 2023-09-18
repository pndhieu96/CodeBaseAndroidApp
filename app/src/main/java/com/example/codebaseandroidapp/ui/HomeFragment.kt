package com.example.codebaseandroidapp.ui

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.adapter.HomeParentRecycleViewAdapter
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentHomeBinding
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.utils.Utils.Companion.observer
import com.example.codebaseandroidapp.viewModel.HomeViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val adapter: HomeParentRecycleViewAdapter by inject()
    private val viewModel: HomeViewModel by viewModel()

    override fun initObserve() {
        viewModel.moviesWithGenre.observer(
            viewLifecycleOwner,
            onSuccess = {
                showList(it)
            },
            onError = {
                showError(it.statusMessage)
            },
            onLoading = {
                showLoadding()
            }
        )
    }

    override fun initialize() {
        binding.recycleViewParent.adapter = adapter
        binding.recycleViewParent.layoutManager = LinearLayoutManager(activity)
        adapter.setCallBack(MovieListen {
            val bundle = bundleOf("movieId" to it.id.toString())
            navController.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        })
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.moviesWithGenre.value?.data.isNullOrEmpty()
            || viewModel.moviesWithGenre.value?.data!!.size == 0
        ) {
            viewModel.getGenres()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (requireActivity() is MainActivity) {
                        (requireActivity() as MainActivity).onBackPress()
                    }
                }
            }
        )
    }

    private fun showError(text: String) {
        binding.tvError.text = (text)
        binding.progressBar.visibility = GONE
        binding.tvError.visibility = VISIBLE
    }

    private fun showLoadding() {
        binding.progressBar.visibility = VISIBLE
        binding.tvError.visibility = GONE
    }

    private fun showList(list: List<MoviesWithGenre>) {
        adapter.submitList(list)
        binding.progressBar.visibility = GONE
        binding.tvError.visibility = GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}