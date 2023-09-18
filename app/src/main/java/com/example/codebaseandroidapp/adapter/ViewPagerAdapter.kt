package com.example.codebaseandroidapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.codebaseandroidapp.ui.HomeRootFragment
import com.example.codebaseandroidapp.ui.SearchRootFragment

class ViewPagerAdapter(
    val fragmentManager: FragmentManager,
    val lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    var currentFragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return HomeRootFragment.newInstance()
            1 -> return SearchRootFragment.newInstance()
        }
        return HomeRootFragment.newInstance()
    }

    override fun getItemId(position: Int): Long {
        currentFragment = fragmentManager.findFragmentByTag("f"+position)
        return super.getItemId(position)
    }
}
