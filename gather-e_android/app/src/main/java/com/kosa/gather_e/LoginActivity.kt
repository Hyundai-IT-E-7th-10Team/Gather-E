package com.kosa.gather_e

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.databinding.ActivityLoginBinding
import com.kosa.gather_e.util.LoginUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val loadingDialog = CircleProgressDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)

        //카카오 로그인 설정
        binding.kakaoLoginBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(
                            this,
                            callback = kakaoLoginHandler
                        )
                    } else if (token != null) {
                        kakaoLogin(token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLoginHandler)
            }
        }

        //구글 로그인 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("356799775533-tluao51i97147innj7vnmfed2kqj02df.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleLoginBtn.setOnClickListener {
            resultLauncher.launch(mGoogleSignInClient.signInIntent)
        }
        setContentView(binding.root)
    }

    private val kakaoLoginHandler: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error == null && token != null) {
            kakaoLogin(token.accessToken)
        } else {
            Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                task.result.idToken?.let { googleLogin(it) }
            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }

    private fun kakaoLogin(token: String) {
        loadingDialog.show(supportFragmentManager, loadingDialog.tag)
        CoroutineScope(Dispatchers.IO).launch {
            val loginResponse = LoginUtil.kakaoLogin(token)
            if (loginResponse != null) {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        BottomNavigationVarActivity::class.java
                    )
                )
                loadingDialog.dismiss()
            } else {
                Toast.makeText(this@LoginActivity, "로그인에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        }
    }

    private fun googleLogin(token: String) {
        loadingDialog.show(supportFragmentManager, loadingDialog.tag)
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("kww", token)
            val loginResponse = LoginUtil.googleLogin(token)
            if (loginResponse != null) {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        BottomNavigationVarActivity::class.java
                    )
                )
                loadingDialog.dismiss()
            } else {
                Toast.makeText(this@LoginActivity, "로그인에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        }
    }
}