package com.kosa.gather_e

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.util.CurrUser
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.util.LoginUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            true
        }

        KakaoSdk.init(this, "da3fb6c1f547e9e4707b46e0e748cc80")

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { token, error ->
                if (error == null) {
                    AuthApiClient.instance.tokenManagerProvider.manager.getToken()
                        ?.let { kakaoLogin(it.accessToken) }
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
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            account.idToken?.let { googleLogin(it) }
        }
    }

    private fun kakaoLogin(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val loginResponse = LoginUtil.kakaoLogin(token)
            if (loginResponse != null) {
                startActivity(Intent(this@SplashActivity, BottomNavigationVarActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun googleLogin(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("kww", token)
            val loginResponse = LoginUtil.googleLogin(token)
            if (loginResponse != null) {
                startActivity(Intent(this@SplashActivity, BottomNavigationVarActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}