package com.example.stagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignupActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    val signup_email_edittext : EditText = findViewById(R.id.signup_email_edittext)
    val signup_password_edittext : EditText = findViewById(R.id.signup_password_edittext)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        val backto_loginActivity_button : Button = findViewById(R.id.backtologinActivity)
        val complete_signup_button : Button = findViewById(R.id.complete_signup_button)

        backto_loginActivity_button.setOnClickListener { // 로그인으로 버튼을 클릭하면 로그인 창으로 이동
            startActivity(Intent(this, LoginActivity::class.java))
        }
        complete_signup_button.setOnClickListener { // 회원가입 완료 버튼을 클릭하면 실행
            signup()
        }
    }

    fun signup() {
        if (signup_email_edittext.length() > 1 && signup_password_edittext.length() > 1) {
            auth?.createUserWithEmailAndPassword(signup_email_edittext.text.toString(), signup_password_edittext.text.toString())
                ?.addOnCompleteListener { // 통신 완료가 된 후에 할 일을 적어준다
                        task ->
                    if (task.isSuccessful) { 
                        // 정상적으로 이메일과 비밀번호가 전달되면 유저의 정보를
                        // 서버에 저장을 완료하고 메인 액티비티로 이동시킨다
                        goMainActivity(task.result?.user) // 메인 액티비티로의 이동
                    }
                    else { // 에러 혹은 서버 연결이 실패한다면 오류 메세지 toast
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
        else {
            Toast.makeText(this,
                "올바른 이메일 혹은 비밀번호가 아닙니다",
                Toast.LENGTH_LONG).show()
        }
    }

    fun goMainActivity(user:FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java)) // 메인 액티비티 호출
        }
    }
}