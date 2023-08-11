package com.example.codebaseandroidapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.callBack.MovieListen
import com.example.codebaseandroidapp.adapter.SearchAdapter
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentSearchBinding
import com.example.codebaseandroidapp.utils.Utils
import com.example.codebaseandroidapp.utils.Utils.Companion.showKeyBoard
import com.example.codebaseandroidapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    @Inject lateinit var adapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.searchMovies(viewModel.currentKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPagerFlow.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun initObserve() {

    }

    override fun initialize() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        adapter.addLoadStateListener { loadState ->
            Log.d("SearchFragment", loadState.toString())
            if (loadState.refresh is LoadState.Loading) {
                binding.progressBar.visibility = VISIBLE
            } else {
                binding.progressBar.visibility = GONE

                // getting the error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                if(error?.error?.message.equals("-1")) {
                    binding.tvNotFound.visibility = VISIBLE
                    binding.list.visibility = GONE
                } else {
                    binding.tvNotFound.visibility = GONE
                    binding.list.visibility = VISIBLE
                    error?.let {
                        if(!(it.error is HttpException && (it.error as HttpException).code() == 422)) {
                            Toast.makeText(
                                binding.root.context,
                                it.error.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
        adapter.setSearchCallBack(MovieListen {
            viewModel.backToAnother = true
            Utils.hideKeyboard(requireActivity())
            val bundle = bundleOf("movieId" to it.id.toString())
            navController.navigate(R.id.action_searchFragment_to_detailFragment4, bundle)
        })
        binding.list.layoutManager = layoutManager
        binding.list.adapter = adapter

        binding.ibtnClose.setOnClickListener {
            binding.etSearch.setText("")
        }

        binding.ibtnSearch.setOnClickListener {
            focusEditText()
        }

        binding.etSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(viewModel.currentKey != s.toString()) {
                    viewModel.searchMovies(s.toString())
                    lifecycleScope.launch {
                        // Paging-2
                        // collectLatest: lắng nghe khi một padingData được emit từ Flow
                        viewModel.searchPagerFlow.collectLatest {
                            adapter.submitData(it)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(!viewModel.backToAnother) {
            focusEditText()
        }
        viewModel.backToAnother = false
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

    private fun focusEditText() {
        binding.etSearch.setText("")
        binding.etSearch.postDelayed({
            binding.etSearch.requestFocus()
            binding.etSearch.let {
                showKeyBoard(requireActivity(), it)
            }
        }, 30)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}