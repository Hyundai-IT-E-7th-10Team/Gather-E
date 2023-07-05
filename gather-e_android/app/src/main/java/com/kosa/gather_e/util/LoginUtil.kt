package com.kosa.gather_e.util

import android.util.Log
import com.kosa.gather_e.model.entity.user.LoginResponse
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider


object LoginUtil {
    fun kakaoLogin(token: String) : LoginResponse? {
        Log.d("gather", token)
        val res = SpringRetrofitProvider.getRetrofit().kakaoLogin(token).execute()
        return if (res.isSuccessful ) {
            SpringRetrofitProvider.init(res.body()!!.accessToken)
            CurrUser.setCurrUser(res.body()!!.user)
            CurrUser.setToken()
            res.body()
        } else {
            null
        }
    }
}