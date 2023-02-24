package com.example.codebaseandroidapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.adapter.MovieListen
import com.example.codebaseandroidapp.adapter.MyListAdapter
import com.example.codebaseandroidapp.databinding.FragmentMyListBinding
import com.example.codebaseandroidapp.viewModel.MyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyListFragment : BaseFragment<FragmentMyListBinding>(FragmentMyListBinding::inflate) {

    @Inject lateinit var adapter: MyListAdapter
    private val viewModel: MyListViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MyListFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyListFragment", "onCreate")
        adapter = MyListAdapter()
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                Log.d("MyListAdapter", "onChanged")
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                Log.d("MyListAdapter", "onItemRangeRemoved")
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                Log.d("MyListAdapter", "onItemRangeMoved")
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                Log.d("MyListAdapter", "onItemRangeInserted")
                (binding?.recycleView.layoutManager as GridLayoutManager).scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                Log.d("MyListAdapter", "onItemRangeChanged")
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                Log.d("MyListAdapter", "onItemRangeChanged")
            }
        })
    }

    override fun FragmentMyListBinding.initialize() {
        Log.d("MyListFragment", "onCreateView")

        binding.recycleView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleView.adapter = adapter

        adapter.setSearchCallBack(MovieListen{
            val bundle = bundleOf("movieId" to it.id.toString())
            navController.navigate(R.id.action_myListFragment_to_detailFragment, bundle)
        })

        binding.progressBar.visibility = View.VISIBLE
        viewModel.myList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyListFragment", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MyListFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyListFragment", "onResume")
        viewModel.getMyList()
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(requireActivity() is MainActivity) {
                        (requireActivity() as MainActivity).onBackPress()
                    }
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        Log.d("MyListFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MyListFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MyListFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyListFragment", "onDestroy")
    }


    override fun onDetach() {
        super.onDetach()
        Log.d("MyListFragment", "onDetach")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MyListFragment().apply {

            }
    }
}