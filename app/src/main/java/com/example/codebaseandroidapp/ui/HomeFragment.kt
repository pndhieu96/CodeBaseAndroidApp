package com.example.codebaseandroidapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.adapter.HomeParentRecycleViewAdapter
import com.example.codebaseandroidapp.adapter.MovieListen
import com.example.codebaseandroidapp.databinding.FragmentHomeBinding
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class homeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

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

    override fun onAttach(context: Context) {
        Log.d("LC-homeFragment", "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LC-homeFragment", "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun FragmentHomeBinding.initialize() {
        Log.d("LC-homeFragment", "onCreateView")

        binding.recycleViewParent.adapter = adapter
        binding.recycleViewParent.layoutManager = LinearLayoutManager(activity)
        showLoadding()
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

        viewModel.moviesWithGenre.observe(viewLifecycleOwner) {
            it?.let {
                showList(it)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                showError(it.message.toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("LC-homeFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d("LC-homeFragment", "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("LC-homeFragment", "onResume")
        super.onResume()

        if (viewModel.moviesWithGenre.value.isNullOrEmpty()
            || viewModel.moviesWithGenre.value?.size == 0
        ) {
            showLoadding()
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

    override fun onPause() {
        Log.d("LC-homeFragment", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("LC-homeFragment", "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("LC-homeFragment", "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("LC-homeFragment", "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("LC-homeFragment", "onDetach")
        super.onDetach()
    }

    fun showError(text: String) {
        binding.tvError.setText(text)
        binding.progressBar.visibility = GONE
        binding.tvError.visibility = VISIBLE
    }

    fun showLoadding() {
        binding.progressBar.visibility = VISIBLE
        binding.tvError.visibility = GONE
    }

    fun showList(list: List<MoviesWithGenre>) {
        adapter.submitList(list)
        binding.progressBar.visibility = GONE
        binding.tvError.visibility = GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            homeFragment().apply {

            }
    }
}