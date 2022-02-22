package com.example.stagram.navigation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stagram.MainActivity
import com.example.stagram.R
import com.example.stagram.databinding.FragmentGalleryBinding
import com.example.stagram.model.ContentDTO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*


class GalleryFragment(uri: Uri) : Fragment() {
    private lateinit var mBinding : FragmentGalleryBinding
    private var uri : Uri? = uri
    private var auth : FirebaseAuth? = null
    private var store : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentGalleryBinding.inflate(inflater, container, false)

        // firebase 서비스 초기화
        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()

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

        // 이미지 파일 업로드
        imageRef.putFile(uri).continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            // firebase storage에 업로드된 이미지의 downloadUrl을 리턴한다
            return@continueWithTask imageRef.downloadUrl}.addOnSuccessListener {
            // ContentDTO 데이터 클래스 생성 (내가 올린 게시물을 데이터화)
            var contentDTO : ContentDTO = ContentDTO()
            contentDTO.explain = mBinding.addphotoEditExplain.text.toString()
            contentDTO.imageUrl = it.toString()
            contentDTO.uid = auth!!.currentUser!!.uid
            contentDTO.userEmail = auth!!.currentUser!!.email
            contentDTO.timestamp = System.currentTimeMillis()

            // 데이터 베이스에 데이터를 저장
            store!!.collection("posts").document().set(contentDTO)
            // 데이터 베이스에 저장이 완료된 후 home으로 이동
            (activity as MainActivity).changeFragment(DetailViewFragment())
        }.addOnFailureListener {
            println(it)
            Toast.makeText(activity, getString(R.string.upload_fail), Toast.LENGTH_SHORT).show()

        }
    }
}