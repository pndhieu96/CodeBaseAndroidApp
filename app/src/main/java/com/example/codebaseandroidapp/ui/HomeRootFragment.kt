package com.example.codebaseandroidapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.databinding.FragmentHomeRootBinding
import com.example.codebaseandroidapp.viewModel.HomeRootViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeRootFragment : BaseFragment<FragmentHomeRootBinding>(FragmentHomeRootBinding::inflate) {

    /*
    * Navigation-1
    * Thành phần Navigation có 3 thành phần chính:
    *
    * - Navigation Graph (new xml resource): Đây là resource chứ thông tin liên quan đến Navigation
    * trong 1 chỗ tập trung. Nó bao gồm toàn bộ các vị trí trong ứng dụng của bạn như là 1 điểm đến hay
    * như nững đường dẫn khả thi mà người dùng có thể đi qua ứng dụng của bạn.
    *
    * - NavHostFragment (layout XML view): đây là 1 widget đặc biệt mà có thể thêm vào trong layout.
    * Nó hiển thị những điểm đến khác nhau từ Navigation Graph
    *
    * - NavController (Kotlin/ java object): đây là một đối tượng theo dõi vị trí hiện tại trong
    * Navigation Graph. Nó sắp xếp hoán đổi nội dụng trong NavHostFragment như khi người dùng di chuyển
    * thông qua Navigation Graph
    * */

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LC-HomeRootFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LC-HomeRootFragment", "onCreate")
    }

    override fun FragmentHomeRootBinding.initialize() {
        Log.d("LC-HomeRootFragment", "onCreateView")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LC-HomeRootFragment", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LC-HomeRootFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LC-HomeRootFragment", "onResume")
    }

    override fun onPause() {
        Log.d("LC-HomeRootFragment", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("LC-HomeRootFragment", "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("LC-HomeRootFragment", "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("LC-HomeRootFragment", "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("LC-HomeRootFragment", "onDetach")
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeRootFragment()
    }
}