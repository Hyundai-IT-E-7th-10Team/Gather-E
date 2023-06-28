package com.kosa.gather_e.model.repository.spring

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpringRetrofitProvider {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val retrofit =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofit(): SpringInterface {
        return retrofit.create(SpringInterface::class.java);
    }
}