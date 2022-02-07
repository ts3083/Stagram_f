package com.example.stagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.stagram.navigation.AlarmFragment
import com.example.stagram.navigation.DetailViewFragment
import com.example.stagram.navigation.GridFragment
import com.example.stagram.navigation.UserFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.navigation.NavigationBarMenu
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

abstract class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottom_navigation : BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottom_navigation.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.action_home -> {
                        bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.black)
                        bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.black)
                        DetailViewFragment()
                        // Respond to navigation item 1 click
                    }
                    R.id.action_search -> {
                        bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.design_default_color_primary_variant)
                        bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.design_default_color_primary_variant)
                        GridFragment()
                        // Respond to navigation item 2 click
                    }
                    R.id.action_favorite_alarm -> {
                        bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.bottom_nav_color)
                        bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.bottom_nav_color)
                        AlarmFragment()
                        // Respond to navigation item 3 click
                    }
                    R.id.action_account -> {
                        bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.browser_actions_bg_grey)
                        bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.browser_actions_bg_grey)
                        UserFragment()
                        // Respond to navigation item 3 click
                    }
                    else -> {
                        bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.black)
                        bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.black)
                        DetailViewFragment()
                    }
                }
            )
            true
        }
        bottom_navigation.selectedItemId = R.id.main_content
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
    }




    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> {
                val detailViewFragment = DetailViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,
                    detailViewFragment).commit()
                return true
            }
            R.id.action_search -> {
                val gridFragment = GridFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,
                    gridFragment).commit()
                return true
            }
            R.id.action_add_photo -> {

                return true
            }
            R.id.action_favorite_alarm -> {
                val alarmFragment = AlarmFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,
                    alarmFragment).commit()
                return true
            }
            R.id.action_account -> {
                val userFragment = UserFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,
                    userFragment).commit()
                return true
            }
        }
        return false
    }*/

}












