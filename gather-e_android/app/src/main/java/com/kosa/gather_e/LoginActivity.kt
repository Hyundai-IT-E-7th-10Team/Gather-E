package com.kosa.gather_e

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.databinding.ActivityLoginBinding
import com.kosa.gather_e.util.CurrUser
import com.kosa.gather_e.model.entity.user.JwtToken
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val loadingDialog = CircleProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.kakaoLoginBtn.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("gather", "카카오톡으로 로그인 실패", error)
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(
                            this,
                            callback = kakaoLoginHandler
                        )
                    } else if (token != null) {
                        getJwtToken(token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLoginHandler)
            }
        }
        setContentView(binding.root)
    }

    private val kakaoLoginHandler: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d("gather", "카카오 로그인 실패")
        } else if (token != null) {
            getJwtToken(token.accessToken)
            Log.d("gather", "카카오 로그인 성공")
        }
    }

    private fun getJwtToken(token: String) {
        loadingDialog.show(supportFragmentManager, loadingDialog.tag)
        Log.d("gather", token)
        val callLogin = SpringRetrofitProvider.getRetrofit().login(token)
        callLogin.enqueue(object : Callback<JwtToken> {
            override fun onFailure(call: Call<JwtToken>, t: Throwable) {
                Log.d("gather", "토큰 요청 실패")
                loadingDialog.dismiss()
            }
            override fun onResponse(call: Call<JwtToken>, response: Response<JwtToken>) {
                val token = response.body()?.accessToken
                Log.d("gather", "jwt : $token")
                val main = Intent(
                    this@LoginActivity,
                    BottomNavigationVarActivity::class.java
                )
                if (token != null) {
                    SpringRetrofitProvider.init(token)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("gather", "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            CurrUser.setCurrUser(user)
                            CurrUser.setSeq(response.body()!!.userSeq)
                        }
                    }
                    loadingDialog.dismiss()
                    startActivity(main)
                }
            }
        })
    }
}