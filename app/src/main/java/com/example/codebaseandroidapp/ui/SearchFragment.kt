package com.example.codebaseandroidapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.adapter.MovieListen
import com.example.codebaseandroidapp.adapter.SearchAdapter
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
class SearchFragment : Fragment() {
    private var binding: FragmentSearchBinding? = null
    private lateinit var navController: NavController
    @Inject
    lateinit var adapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.searchMovies(viewModel.currentKey)
        lifecycleScope.launch {
            viewModel.searchPagerFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        navController = findNavController(this)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        // Paging-3
        // addLoadStateListener truy???n v??o 1 CombinedLoadStates: ????? l???ng nghe tr???ng th??i hi???n t???i c???a vi???c load d??? li???u nh??
            // CombinedLoadStates.refresh: ?????i di???n cho tr???ng th??i khi b???t ?????u t???i
            // CombinedLoadStates.prepend: ?????i di???n cho tr???ng th??i t???i khi b???t ?????u danh s??ch
            // CombinedLoadStates.append: ?????i di???n cho tr???ng th??i t???i khi ??? cu???i danh s??ch
        adapter.addLoadStateListener { loadState ->
            Log.d("SearchFragment", loadState.toString())
            if (loadState.refresh is LoadState.Loading) {
                binding?.progressBar?.visibility = VISIBLE
            } else {
                binding?.progressBar?.visibility = View.GONE

                // getting the error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                if(error?.error?.message.equals("-1")) {
                    binding?.tvNotFound?.visibility = VISIBLE
                    binding?.list?.visibility = GONE
                } else {
                    binding?.tvNotFound?.visibility = GONE
                    binding?.list?.visibility = VISIBLE
                    error?.let {
                        if(!(it.error is HttpException && (it.error as HttpException).code() == 422)) {
                            Toast.makeText(
                                binding?.root?.context,
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
        binding?.list?.layoutManager = layoutManager
        binding?.list?.adapter = adapter

        binding?.ibtnClose?.setOnClickListener {
            binding?.etSearch?.setText("")
        }

        binding?.ibtnSearch?.setOnClickListener {
            focusEditText()
        }

        binding?.etSearch?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(!viewModel.currentKey.equals(s.toString())) {
                    viewModel.searchMovies(s.toString())
                    lifecycleScope.launch {
                        // Paging-2
                        // collectLatest: l???ng nghe khi m???t padingData ???????c emit t??? Flow
                        viewModel.searchPagerFlow.collectLatest {
                            adapter.submitData(it)
                        }
                    }
                }
            }
        })
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun focusEditText() {
        binding?.etSearch?.setText("")
        binding?.etSearch?.postDelayed(Runnable {
            binding?.etSearch?.requestFocus()
            binding?.etSearch?.let {
                showKeyBoard(requireActivity(), it)
            }
        }, 30)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {

            }
    }
}