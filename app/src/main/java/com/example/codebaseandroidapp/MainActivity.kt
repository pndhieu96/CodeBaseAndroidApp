package com.example.codebaseandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.codebaseandroidapp.databinding.ActivityMainBinding
import com.example.codebaseandroidapp.adapter.ViewPagerAdapter
import com.example.codebaseandroidapp.ui.SearchRootFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Hilt-2
 * @AndroidEntryPoint:
 * Hilt tạo một bộ chứa những dependencies cần thiết cho lớp android được chú thích, tuân theo vòng đời của nó
 * và cho phép inject những instances của những phụ thuộc đó vào lớp để sử dụng
 * Nó hỗ trợ cho những class thường dùng trong android như:
 * activity, fragment, view, service, broadcastReceiver
 *
 * Field injection:
 * Để lấy những instances cần thiết ra sử dụng, ta chú thích @Inject trên field muốn được inject trong lớp
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TabLayoutMediator.TabConfigurationStrategy {

    private lateinit var binding : ActivityMainBinding
    private lateinit var tabLayoutMediator : TabLayoutMediator
    @Inject
    lateinit var viewPagerAdapter : ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ActivityLifecycle", "onCreate")
        this.supportActionBar!!.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.isUserInputEnabled = false

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager, this)
        tabLayoutMediator.attach()

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        for (i in 0 until binding.tabLayout.childCount) {
            binding.tabLayout.getChildAt(i).setPadding(10, 25, 10, 0)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("ActivityLifecycle", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifecycle", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityLifecycle", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityLifecycle", "onDestroy")
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        //Khi từ fragment 1 chuyển sang fragment 2, fragment 1 sẽ ở trạng thái onPause
        //Khi từ fragment 2 back lại, fragment 1 sẽ chuyển lại trạng thái onResume
        val drawableHome = ContextCompat.getDrawable(this, R.drawable.home)
        val drawableSearch = ContextCompat.getDrawable(this, R.drawable.search)
        val drawableMyList = ContextCompat.getDrawable(this, R.drawable.heart)
        val drawableExtension = ContextCompat.getDrawable(this, R.drawable.extension)
        when(position) {
            0 -> {
                tab.text = "Home"
                tab.setIcon(drawableHome)
            }
            1 -> {
                tab.text = "Search"
                tab.setIcon(drawableSearch)
            }
            2 -> {
                tab.text = "My List"
                tab.setIcon(drawableMyList)
            }
            3 -> {
                tab.text = "Extenstion"
                tab.setIcon(drawableExtension)
            }
        }
    }

    fun onBackPress() {
        if(binding.viewPager.currentItem == 0) {
            finish()
        } else {
            binding.viewPager.setCurrentItem(0)
        }
    }
}