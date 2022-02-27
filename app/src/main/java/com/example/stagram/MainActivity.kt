package com.example.stagram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.stagram.databinding.ActivityMainBinding
import com.example.stagram.navigation.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var launcer = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it -> changeFragment(GalleryFragment(it))
    }

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

        // 메인 화면이 뜨면 detailviewfragment가 뜨도록 설정
        binding.bottomNavigation.selectedItemId = R.id.action_home
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
                        if(ContextCompat.checkSelfPermission(this@MainActivity.applicationContext,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                            launcer.launch("image/*")
                        } else {
                            Toast.makeText(this@MainActivity,
                                "갤러리 접근 권한이 거부되어 있습니다. 설정에서 접근을 허용해 주세요.",
                                Toast.LENGTH_SHORT).show()
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

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
    }

    /*fun goAddPhotoActivity() {
        startActivity(Intent(this, AddPhotoActivity::class.java)) // 메인 액티비티 호출
    }*/
}












