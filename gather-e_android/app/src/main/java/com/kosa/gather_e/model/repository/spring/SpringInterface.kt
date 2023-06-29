package com.kosa.gather_e.model.repository.spring

import com.kosa.gather_e.model.entity.user.JwtToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpringInterface {

    @GET("auth/kakao")
    fun login(@Query("accessToken") token: String): Call<JwtToken>
}