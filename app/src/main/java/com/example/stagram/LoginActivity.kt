package com.example.stagram

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    var googleSignInClient : GoogleSignInClient? = null // 구글의 계정을 저장하는 변수를 선언
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
        val google_signin_button : Button = findViewById(R.id.google_signin_btn)

        loginbtn.setOnClickListener { // 로그인 버튼을 클릭하면 login 함수 실행
            login()
        }
        signupbtn.setOnClickListener { // 회원가입 버튼을 클릭하면 회원가입 창으로 이동
            startActivity(Intent(this, SignupActivity::class.java))
        }
        google_signin_button.setOnClickListener {
            google_login()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // 사용자의 기본적인 정보(userID, basic profile)를 얻기
            .requestIdToken(getString(R.string.default_web_client_id))
                // 필수사항으로 사용자의 식별값을 사용하겠다고 app이 구글에 요청하는 것
            .requestEmail()
                // 사용자의 이메일을 사용하겠다고 app이 구글에 요청하는 것
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // 내 앱에서 구글의 계정을 가져다 쓸거니까 알고 있으라는 뜻
        // 구글에 사용자 정보를 요청하고 gso에 받아오면 googleSignInClient 변수에 저장

    }

    fun google_login() {
        val signInIntent: Intent = googleSignInClient!!.signInIntent // 구글 signin 화면으로의 이동
        startForResult.launch(signInIntent)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val intent: Intent = result.data!!
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e : ApiException) {
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
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