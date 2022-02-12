package com.example.stagram.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.stagram.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var addphoto_image : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        val addphoto_btn_upload : Button = findViewById(R.id.addphoto_btn_upload)
        addphoto_image = findViewById(R.id.addphoto_image)

        // storage에 파이어베이스 storage로 설정
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        // 엘범 화면 열기
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        // 이미지 업로드 이벤트 추가
        addphoto_btn_upload.setOnClickListener {
            contentUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_FROM_ALBUM) {
            if(requestCode == Activity.RESULT_OK) {
                // 사진을 선택했을 때 이미지의 경로
                photoUri = data?.data // photoUri에 사진 경로를 담는다
                addphoto_image?.setImageURI(photoUri) // imageView에 이미지를 띄우기
            }
        }
    }

    fun contentUpload() {
        // 선택한 이미지를 업로드하는 함수
        // 중복 생성 되지 않는 파일명을 만들기
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var ImageFileName = "IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(ImageFileName)
        // 파일 업로드
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(this, getString(R.string.upload_success),Toast.LENGTH_LONG).show()
        }
    }
}