package com.example.codebaseandroidapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.codebaseandroidapp.adapter.ViewPagerAdapter
import com.example.codebaseandroidapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber


class MainActivity : AppCompatActivity(), TabLayoutMediator.TabConfigurationStrategy {

    private lateinit var binding : ActivityMainBinding
    private lateinit var tabLayoutMediator : TabLayoutMediator
    lateinit var viewPagerAdapter : ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        this.supportActionBar!!.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewPagerAdapter = ViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle
        )
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
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("Timber.d(\"onBackPressed\")", "timber.log.Timber")
    )
    override fun onBackPressed() {
        Timber.d("onBackPressed")
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        //Khi từ fragment 1 chuyển sang fragment 2, fragment 1 sẽ ở trạng thái onPause
        //Khi từ fragment 2 back lại, fragment 1 sẽ chuyển lại trạng thái onResume
        val drawableHome = ContextCompat.getDrawable(this, R.drawable.home)
        val drawableSearch = ContextCompat.getDrawable(this, R.drawable.search)
        when(position) {
            0 -> {
                tab.text = "Home"
                tab.icon = drawableHome
            }
            1 -> {
                tab.text = "Search"
                tab.icon = drawableSearch
            }
        }
    }

    fun onBackPress() {
        if(binding.viewPager.currentItem == 0) {
            finish()
        } else {
            binding.viewPager.currentItem = 0
        }
    }
}