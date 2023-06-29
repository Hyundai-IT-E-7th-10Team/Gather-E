package com.kosa.gather_e.model.repository.spring

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpringRetrofitProvider {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"


    // 우원오빠가 짠거 주석처리
/*    private val retrofit =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()*/

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    // Bearer 토큰을 추가
    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        // string.xml에 넣어야 하는데 얘는 fragment같은게 아니라서 어려움 ㅠ
        val token = "eyJyZWdEYXRlIjoxNjg3OTk4MDc1NjEzLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJkeWZmaDEwMzFAZ21haWwuY29tIiwidXNlclNlcSI6MSwic3ViIjoi6rmA7Jqw7JuQIiwiZXhwIjoxNjg4MDM0MDc1fQ.gMXjuMuT6O3NHN1FfufLr8vgWY8py-lHoDY0oqJvAqQ"

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token") // Bearer 토큰을 헤더에 추가
                .build()
            chain.proceed(request)
        }
        return httpClient.build()
    }


    fun getRetrofit(): SpringInterface {
        return retrofit.create(SpringInterface::class.java);
    }
}