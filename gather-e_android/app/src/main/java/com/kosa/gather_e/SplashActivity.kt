package com.kosa.gather_e

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.util.CurrUser
import com.kosa.gather_e.model.entity.user.JwtToken
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         installSplashScreen().setKeepOnScreenCondition{
            true
        }
        KakaoSdk.init(this, "da3fb6c1f547e9e4707b46e0e748cc80")
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { token, error ->
                if (error == null) {
                    AuthApiClient.instance.tokenManagerProvider.manager.getToken()
                        ?.let { getJwtToken(it.accessToken) }
                } else {
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun getJwtToken(token: String) {
        Log.d("gather", token)
        val callLogin = SpringRetrofitProvider.getRetrofit().login(token)
        callLogin.enqueue(object : Callback<JwtToken> {
            override fun onFailure(call: Call<JwtToken>, t: Throwable) {
                Log.d("gather", "토큰 요청 실패")
                intent = Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()

            }
            override fun onResponse(call: Call<JwtToken>, response: Response<JwtToken>) {
                val jwtToken = response.body()!!.accessToken
                Log.d("gather", "jwt : $jwtToken")
                SpringRetrofitProvider.init(jwtToken)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("gather", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        CurrUser.setCurrUser(user)
                        CurrUser.setSeq(response.body()!!.userSeq)
                        CurrUser.setToken()
                    }
                }
                intent = Intent(
                    this@SplashActivity,
                    BottomNavigationVarActivity::class.java
                )
                startActivity(intent)
                finish()

            }
        })
    }
}