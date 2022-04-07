package com.example.codebaseandroidapp

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

/**
 * Hilt-2
 * @AndroidEntryPoint:
 * Tạo một bộ chứa những dependencies cần thiết cho android class, tuân theo vòng đời của android class
 * và cho phép inject những instances của những phụ thuộc đó vào lớp để sử dụng
 * Nó hỗ trợ cho những class thường dùng trong android như:
 * activity, fragment, view, service, broadcastReceiver
 *
 * Field injection:
 * Để lấy những instances cần thiết để sử dụng, ta chú thích @Inject trên field muốn được inject
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TabLayoutMediator.TabConfigurationStrategy {

    private lateinit var binding : ActivityMainBinding
    private var viewPagerAdapter : ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LC-MainActivity", "onCreate")
        this.supportActionBar!!.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.isUserInputEnabled = false
        var tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager, this)
        tabLayoutMediator.attach()
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPagerAdapter?.currentFragment?.let {
                    if(it is SearchRootFragment)
                        it.navToRootDestination()
                }
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
        Log.d("LC-MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LC-MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LC-MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LC-MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LC-MainActivity", "onDestroy")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("LC-MainActivity", "onBackPressed")
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