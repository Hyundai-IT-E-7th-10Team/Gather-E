package com.kosa.gather_e.model.repository.firebase

import android.util.Log
import com.bumptech.glide.load.engine.Resource
import com.google.firebase.messaging.FirebaseMessaging
import com.kosa.gather_e.R
import com.kosa.gather_e.model.repository.spring.SpringInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FCMRetrofitProvider {
//    R.string.
    private var token: String = "AAAAUDl3SRs:APA91bGXkbxEXDb2zBvQYyi5mBD-cvcdtSfLWALDZZ11R47GbtAUzr1x8Q1Uiyj43HbKKHit-XfhujDIVTOiiTUV3UQpcuL0h2bYW-TiVX-91krPWZxzd3UuHwHQbyH2AALN8It4KpFm"
    private const val BASE_URL = "https://fcm.googleapis.com/"

    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }

        return httpClient.build()
    }

//    fun setToken(token: String) {
//        this.token = token
//        // Rebuild the Retrofit instance with the updated token
//        retrofit = retrofit.newBuilder()
//            .client(createOkHttpClient())
//            .build()
//    }

    fun getRetrofit(): NotificationInterface {
        return retrofit.create(NotificationInterface::class.java)
    }
}
