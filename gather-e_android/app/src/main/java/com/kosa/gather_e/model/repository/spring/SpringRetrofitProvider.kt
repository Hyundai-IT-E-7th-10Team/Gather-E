package com.kosa.gather_e.model.repository.spring

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpringRetrofitProvider {
//    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    private const val BASE_URL = "http://ryulrudaga.com:23457/api/"
    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Bearer 토큰을 추가
    private fun createOkHttpClient(token: String): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        Log.d("gather", "$token")

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token") // Bearer 토큰을 헤더에 추가
                .build()
            chain.proceed(request)
        }
        return httpClient.build()
    }

    fun init(token: String) {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient(token))
            .build()
    }

    fun getRetrofit(): SpringInterface {
        return retrofit.create(SpringInterface::class.java);
    }
}