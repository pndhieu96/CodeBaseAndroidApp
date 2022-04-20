package com.example.codebaseandroidapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.adapter.HomeParentRecycleViewAdapter
import com.example.codebaseandroidapp.adapter.MovieListen
import com.example.codebaseandroidapp.databinding.FragmentHomeBinding
import com.example.codebaseandroidapp.model.MoviesWithGenre
import com.example.codebaseandroidapp.viewModel.HomeViewModel
import com.example.codebaseandroidapp.viewModel.HomeViewModelFactory
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class homeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var navController: NavController

    @Inject
    lateinit var adapter: HomeParentRecycleViewAdapter

    //    cách khác để khởi tạo 1 viewmodel từ by viewModels
//    private val viewModel: HomeViewModel by viewModels{
//        HomeViewModelFactory((requireActivity().application as Application).repository)
//    }
//    inject instance cua viewmodel
    private val viewModel: HomeViewModel by viewModels()

    override fun onAttach(context: Context) {
        Log.d("LC-homeFragment", "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LC-homeFragment", "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LC-homeFragment", "onCreateView")
        binding = FragmentHomeBinding.inflate(inflater)

//      Navigation-4
//      NavController
//      Dùng findNavController trong fragment là 1 destination trong navGraph để lấy được
//      instance của 1 navController
        navController = findNavController(this)

//      cách khác để khởi tạo 1 viewmodel từ viewModelFactory
//      viewModel = ViewModelProvider(this,
//          HomeViewModelFactory((requireActivity().application as Application).repository)
//      ).get(HomeViewModel::class.java)

        binding!!.recycleViewParent.adapter = adapter
        binding!!.recycleViewParent.layoutManager = LinearLayoutManager(activity)
        showLoadding()
        adapter.setCallBack(MovieListen {
//          Navigation-5
//          NavController
//          Dùng navCOntroller.navigate để navigate từ destination hiện tại đến 1 destination khác
//          trong navGraph và truyền vào action hoặc id của destination tương ứng kèm các arguments cần
//          thiết dưới dạng 1 bundle
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


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("LC-homeFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("LC-homeFragment", "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
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
        binding = null
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
        binding!!.tvError.setText(text)
        binding!!.progressBar.visibility = GONE
        binding!!.tvError.visibility = VISIBLE
    }

    fun showLoadding() {
        binding!!.progressBar.visibility = VISIBLE
        binding!!.tvError.visibility = GONE
    }

    fun showList(list: List<MoviesWithGenre>) {
        adapter.submitList(list)
        binding!!.progressBar.visibility = GONE
        binding!!.tvError.visibility = GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            homeFragment().apply {

            }
    }
}