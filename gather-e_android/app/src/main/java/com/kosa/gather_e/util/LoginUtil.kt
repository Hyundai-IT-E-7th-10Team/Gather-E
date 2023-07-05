package com.kosa.gather_e.util

import android.util.Log
import com.kosa.gather_e.model.entity.user.LoginResponse
import com.kosa.gather_e.model.entity.user.UserEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider


object LoginUtil {

    private fun init(user: LoginResponse) {
        SpringRetrofitProvider.init(user.accessToken)
        CurrUser.setCurrUser(user.user)
        CurrUser.setToken()
    }

    fun kakaoLogin(token: String) : LoginResponse? {
        val res = SpringRetrofitProvider.getRetrofit().kakaoLogin(token).execute()
        return if (res.isSuccessful ) {
            init(res.body()!!)
            res.body()
        } else {
            null
        }
    }

    fun googleLogin(token: String) : LoginResponse? {
        val res = SpringRetrofitProvider.getRetrofit().googleLogin(token).execute()
        Log.d("kww", "여긴 오니?")
        return if (res.isSuccessful ) {
            init(res.body()!!)
            res.body()
        } else {
            null
        }
    }
}