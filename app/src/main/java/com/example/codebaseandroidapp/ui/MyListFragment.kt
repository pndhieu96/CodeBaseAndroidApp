package com.example.codebaseandroidapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.adapter.MyListAdapter
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.base.MainBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentMyListBinding
import com.example.codebaseandroidapp.utils.Utils.Companion.observer
import com.example.codebaseandroidapp.viewModel.MyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyListFragment : MainBaseFragment<FragmentMyListBinding>(FragmentMyListBinding::inflate) {

    @Inject lateinit var adapter: MyListAdapter
    private val viewModel: MyListViewModel by viewModels()

    override fun initObserve() {
        viewModel.myList.observer(
            viewLifecycleOwner,
            onSuccess = {
                adapter.submitList(it)
                binding.progressBar.visibility = View.GONE
            },
            onLoading = {
                binding.progressBar.visibility = View.VISIBLE
            }
        )
    }

    override fun initialize() {
        binding.recycleView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycleView.adapter = adapter

        adapter.setSearchCallBack(MovieListen{
            val bundle = bundleOf("movieId" to it.id.toString())
            navController.navigate(R.id.action_myListFragment_to_detailFragment, bundle)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                (binding.recycleView.layoutManager as GridLayoutManager).scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                Log.d("MyListAdapter", "onItemRangeChanged")
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                Log.d("MyListAdapter", "onItemRangeChanged")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyList()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyListFragment()
    }
}