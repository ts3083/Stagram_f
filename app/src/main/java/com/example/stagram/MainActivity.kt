package com.example.stagram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.stagram.databinding.ActivityMainBinding
import com.example.stagram.navigation.*
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // xml에 씌여져있는 view의 정의를 실제 view 객체로 만드는 역할
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationBar()

        // 엡에서 접근을 허용할지 말지 선택하는 창
        // 한번 허용하면 앱이 설치되어 있는 동안 다시 묻지 않음
       ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        , 1)

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
                        if(ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                            goAddPhotoActivity()
                        }

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

    fun goAddPhotoActivity() {
        startActivity(Intent(this, AddPhotoActivity::class.java)) // 메인 액티비티 호출
    }
}












