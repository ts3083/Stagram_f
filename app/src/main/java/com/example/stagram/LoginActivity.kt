package com.example.stagram

import android.app.Activity
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

    // 내 앱에서 구글의 계정을 가져다 쓸거니 알고 있으라는 뜻으로 googleSignInClient를 선언언
   var googleSignInClient : GoogleSignInClient? = null // 구글의 계정을 저장하는 변수를 선언
    var email_login_text : EditText? = null
    var password_login_text : EditText? = null
    val GOOGLE_LOGIN_CODE = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email_login_text = findViewById(R.id.email_edittext)
        password_login_text = findViewById(R.id.password_edittext)
        val loginbtn : Button = findViewById(R.id.email_login_button)
        val signupbtn : Button = findViewById(R.id.email_signup_button)
        val google_signin_btn : Button = findViewById(R.id.google_signin_btn)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // 사용자의 기본적인 정보(userID, basic profile)를 얻기
            .requestIdToken(getString(R.string.default_web_client_id))
            // 필수사항으로 사용자의 식별값을 사용하겠다고 app이 구글에 요청하는 것
            .requestEmail()
            // 사용자의 이메일을 사용하겠다고 app이 구글에 요청하는 것
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // 구글에 사용자 정보를 요청하고 gso에 받아오면 googleSignInClient 변수에 저장
        google_signin_btn.setOnClickListener {
            google_login()
        }
        loginbtn.setOnClickListener { // 로그인 버튼을 클릭하면 login 함수 실행
            login()
        }
        signupbtn.setOnClickListener { // 회원가입 버튼을 클릭하면 회원가입 창으로 이동
            startActivity(Intent(this, SignupActivity::class.java))
        }


    }

    fun google_login() {
        var signInIntent = googleSignInClient?.signInIntent // 구글 로그인 화면을 띄움
        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
    }

    // startActivityResult()로 인해 다른 앱/액티비티가 실행 된 후,
    // 그 앱 / 액티비티의 작업이 끝나서 다시 이 액티비티로 돌아왔을 때 onActivityResult가 실행됨
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Activity.Result_OK : 정상완료
        // Activity.Result_CANCEL : 중간에 취소되었음(실패)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                // 구글 정보가 정상 완료되었다면 결과 Intent(data 매개변수)에서 구글로그인 결과 꺼내오기
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
                // 정상적으로 로그인되었다면 아래 조건문 실행 - firebase에 사용자 이메일정보보내기
                if (result != null) {
                    if (result.isSuccess) {
                        val account = result.signInAccount
                        firebaseAuthWithGoogle(account)
                    }
                }
            }
        }
    }

    /*fun google_login() {
        val signInIntent : Intent? = googleSignInClient?.signInIntent // 구글 signin 화면으로의 이동
        startForResult.launch(signInIntent)
    }*/

    /*var startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent = result.data!!
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account)
            } catch (e : ApiException) {
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }*/

    // 정상적으로 로그인 시 서버에 사용자 정보를 보내주는 함수
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        // 구글로부터 로그인된 사용자의 정보(Credentail)를 얻어온다
        val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)?.addOnCompleteListener {
            // 통신 완료가 된 후에 할 일을 적어준다
                    task ->
                if (task.isSuccessful) { // 로그인 정보가 존재한다면 로그인 처리를 해준다
                    goMainActivity(auth?.currentUser) // 메인 액티비티로의 이동
                } else { // 로그인 정보가 존재하지 않는다면 오류 메세지 toast
                    Toast.makeText(this, "사용자 정보가 존재하지 않습니다",Toast.LENGTH_LONG).show()
                }
            }
    }

    fun login(){
        if (email_login_text?.length()!! > 1 && password_login_text?.length()!! > 1) {
            auth?.signInWithEmailAndPassword(
                email_login_text?.text.toString(),
                password_login_text?.text.toString()
            )
                ?.addOnCompleteListener { // 통신 완료가 된 후에 할 일을 적어준다
                        task ->
                    if (task.isSuccessful) { // 로그인 정보가 존재한다면 로그인 처리를 해준다
                        goMainActivity(task.result?.user) // 메인 액티비티로의 이동
                    } else { // 로그인 정보가 존재하지 않는다면 오류 메세지 toast
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun goMainActivity(user:FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java)) // 메인 액티비티 호출
            finish()
        }
    }
}