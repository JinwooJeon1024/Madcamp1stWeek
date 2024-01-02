package com.example.madcamp1stweek

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.madcamp1stweek.ui.home.HomeFragment
import com.example.madcamp1stweek.ui.dashboard.DashboardFragment
import com.example.madcamp1stweek.ui.notifications.NotificationsFragment
class MainActivity : AppCompatActivity() {

    private val vp: ViewPager2 by lazy {
        findViewById(R.id.pager)
    }

    private val bn: BottomNavigationView by lazy {
        findViewById(R.id.nav_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp.apply {
            adapter = ViewPagerAdapter(this@MainActivity)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bn.selectedItemId = when (position) {
                        0 -> R.id.navigation_home
                        1 -> R.id.navigation_dashboard
                        else -> R.id.navigation_notifications
                    }
                }
            })
        }

        bn.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> vp.currentItem = 0
                R.id.navigation_dashboard -> vp.currentItem = 1
                else -> vp.currentItem = 2
            }
            true
        }
    }

    inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> DashboardFragment()
                else -> NotificationsFragment()
            }
        }
    }
}
