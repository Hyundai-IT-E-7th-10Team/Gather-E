package com.kosa.gather_e

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        setContentView(binding.root)
    }

    private val kakaoLoginHandler: (OAuthToken?, Throwable?) -> Unit = { token, error ->
         if (error == null && token != null) {
            kakaoLogin(token.accessToken)
        } else {
             Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show()
         }
    }

    private fun kakaoLogin(token: String) {
        loadingDialog.show(supportFragmentManager, loadingDialog.tag)
        CoroutineScope(Dispatchers.IO).launch {
            val loginResponse = LoginUtil.kakaoLogin(token)
            if (loginResponse != null) {
                startActivity(Intent(
                    this@LoginActivity,
                    BottomNavigationVarActivity::class.java
                ))
                loadingDialog.dismiss()
            } else {
                Toast.makeText(this@LoginActivity, "로그인에 실패 하였습니다.",Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        }
    }
}