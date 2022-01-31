package com.example.stagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    var email_login_text : EditText? = null
    var password_login_text : EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email_login_text = findViewById(R.id.email_edittext)
        password_login_text = findViewById(R.id.password_edittext)
        val loginbtn : Button = findViewById(R.id.email_login_button)
        val signupbtn : Button = findViewById(R.id.email_signup_button)

        loginbtn.setOnClickListener { // 로그인 버튼을 클릭하면 login 함수 실행
            login()
        }
        signupbtn.setOnClickListener { // 회원가입 버튼을 클릭하면 회원가입 창으로 이동
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    fun login(){
        if (email_login_text?.length()!! > 1 && password_login_text?.length()!! > 1) {
            auth?.signInWithEmailAndPassword(email_login_text?.text.toString(), password_login_text?.text.toString())
                ?.addOnCompleteListener { // 통신 완료가 된 후에 할 일을 적어준다
                    task ->
                    if (task.isSuccessful) { // 로그인 정보가 존재한다면 로그인 처리를 해준다
                        goMainActivity(task.result?.user) // 메인 액티비티로의 이동
                    }
                    else { // 로그인 정보가 존재하지 않는다면 오류 메세지 toast
                        Toast.makeText(this, "사용자 정보가 존재하지 않습니다",Toast.LENGTH_LONG).show()
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