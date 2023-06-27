package com.kosa.gather_e.ui.searchlocation

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.kosa.gather_e.R
import com.kosa.gather_e.data.model.LocationApiResponse
import com.kosa.gather_e.data.model.SearchLocationEntity
import com.kosa.gather_e.data.repository.searchLocation.SearchLocationRepository
import com.kosa.gather_e.databinding.ActivitySearchLocationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.security.MessageDigest

class SearchLocationActivity : AppCompatActivity() {

    private val searchLocationViewModel = SearchLocationViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar의 X(취소) 버튼
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        // 검색 버튼
        binding.searchLocationBtn.setOnClickListener {

        val apiKey = getString(R.string.kakaoRestApiKey)
        val query = binding.searchLocationEdittext.text.toString()

        searchLocationViewModel.getLocationInfo(apiKey, query, object : SearchLocationRepository.GetDataCallback<LocationApiResponse> {

            override fun onSuccess(data: LocationApiResponse) {
                Log.d("gather", "장소 검색 결과 : " + data.toString())
                binding.searchLocationRecyclerView.layoutManager = LinearLayoutManager(this@SearchLocationActivity)
                data?.let {
                    binding.searchLocationRecyclerView.adapter = SearchLocationAdapter(it.documents)
                }
            }

            override fun onFailure(throwable: Throwable) {
                // 데이터 조회 실패 시 처리
            }
        })


        }
    }
}