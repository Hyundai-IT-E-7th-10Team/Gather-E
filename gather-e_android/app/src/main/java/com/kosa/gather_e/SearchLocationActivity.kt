package com.kosa.gather_e

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.kosa.gather_e.databinding.ActivitySearchLocationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.security.MessageDigest

class SearchLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchLocationBtn.setOnClickListener {

            // 카카오 지도 검색 API
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val locationApiService = retrofit.create(SearchLocationApiService::class.java)

            val apiKey = getString(R.string.kakaoRestApiKey)
            val query = binding.searchLocationEdittext.text.toString()
            Log.d("hong", query)
            val locationCall = locationApiService.getLocationData(apiKey, query)

            locationCall.enqueue(object : Callback<LocationApiResponse> {
                override fun onResponse(call: Call<LocationApiResponse>, response: Response<LocationApiResponse>) {
                    val LocationData = response.body()?.documents
                    Log.d("hong", response.code().toString())
                    Log.d("hong", LocationData.toString())
                    Log.d("Test", "Raw: ${response.raw()}")
                    Log.d("Test", "Raw: ${response.body()}")
                }

                override fun onFailure(call: Call<LocationApiResponse>, t: Throwable) {
                    Log.d("hong", "실패")

                }
            })

        }
    }
}