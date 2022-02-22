package com.example.stagram.navigation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stagram.R
import com.example.stagram.databinding.FragmentGalleryBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class GalleryFragment(uri: Uri) : Fragment() {
    private lateinit var mBinding : FragmentGalleryBinding
    private var uri : Uri? = uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentGalleryBinding.inflate(inflater, container, false)

        mBinding.addphotoImage.setImageURI(uri)
        mBinding.addphotoBtnUpload.setOnClickListener {
            uploadImageOFFirebase(uri!!)
        }
        /*val view = LayoutInflater.from(activity).inflate(R.layout.fragment_gallery,
            container, false)*/
        return mBinding.root
    }

    fun uploadImageOFFirebase(uri: Uri) {
        var storage : FirebaseStorage? = FirebaseStorage.getInstance()
        var fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"

        // 파일 로드, 다운로드, 삭제, 메타데이터 가져오기 또는 업데이트를 하기 위해 레퍼런스 생성
        // 참조는 클라우드 파일을 가리키는 포인터라고 할 수 있음
        var imageRef = storage!!.reference.child("images/").child(fileName)

        imageRef.putFile(uri).addOnSuccessListener {
            Toast.makeText(activity, getString(R.string.upload_success), Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            println(it)
            Toast.makeText(activity, getString(R.string.upload_fail), Toast.LENGTH_SHORT).show()

        }
    }
}