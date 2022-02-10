package com.example.stagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.stagram.databinding.ActivityMainBinding
import com.example.stagram.navigation.AlarmFragment
import com.example.stagram.navigation.DetailViewFragment
import com.example.stagram.navigation.GridFragment
import com.example.stagram.navigation.UserFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // xml에 씌여져있는 view의 정의를 실제 view 객체로 만드는 역할
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationBar()

}
    fun initNavigationBar() {
        binding.bottomNavigation.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.action_home -> {
                        changeFragment(DetailViewFragment())
                    }
                    R.id.action_search -> {
                        changeFragment(GridFragment())
                    }
                    R.id.action_add_photo -> {

                    }
                    R.id.action_favorite_alarm -> {
                        changeFragment(AlarmFragment())
                    }
                    R.id.action_account -> {
                        changeFragment(UserFragment())
                    }
                }
                true
            }
            selectedItemId = R.id.action_home
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
    }
}












