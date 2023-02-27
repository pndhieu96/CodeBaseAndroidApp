package com.example.codebaseandroidapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.databinding.FragmentHomeRootBinding
import dagger.hilt.android.AndroidEntryPoint

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

    override fun initialize() {
    }

    override fun initObserve() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeRootFragment()
    }
}