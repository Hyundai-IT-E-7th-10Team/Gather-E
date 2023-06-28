package com.kosa.gather_e.model.repository.kakao

import com.kosa.gather_e.model.repository.spring.SpringInterface
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoRetrofitProvider {

    private const val BASE_URL = "https://dapi.kakao.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofit(): KakaoInterface {
        return retrofit.create(KakaoInterface::class.java);
    }
}