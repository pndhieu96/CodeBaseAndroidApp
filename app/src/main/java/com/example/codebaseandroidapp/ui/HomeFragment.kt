package com.example.codebaseandroidapp.ui

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    /**
     * Navigation-4
     * NavController
     * Dùng findNavController trong fragment mà fragment đó là 1 destination trong navGraph để lấy được
     * instance của 1 navController
     */
    @Inject lateinit var adapter: HomeParentRecycleViewAdapter

    /**
     * cách khác để khởi tạo 1 viewmodel từ by viewModels
     * private val viewModel: HomeViewModel by viewModels{
     *     HomeViewModelFactory((requireActivity().application as Application).repository)
     * }
     */

    /**
     * inject instance của viewmodel với hilt
     */
    private val viewModel: HomeViewModel by viewModels()

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
            /**
             * Navigation-5
             * NavController
             * Dùng navCOntroller.navigate để navigate từ destination hiện tại đến 1 destination khác
             * trong navGraph và truyền vào action hoặc id của destination tương ứng kèm các arguments cần
             * thiết dưới dạng 1 bundle
             *
             * Nếu navigate fragment 1 -> fragment 2, thì fragment 1 sẽ rơi vào trạng thái onStop
             * Khi từ fragment 2 back lại thì fragment 1 sẽ bắt đầu từ trạng thái onCreateView
             */
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