package com.kosa.gather_e.ui.searchlocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kosa.gather_e.R
import com.kosa.gather_e.model.entity.location.LocationApiResponse
import com.kosa.gather_e.model.entity.location.SearchLocationEntity
import com.kosa.gather_e.model.repository.kakao.SearchLocationRepository
import com.kosa.gather_e.databinding.ActivitySearchLocationBinding

class SearchLocationActivity : AppCompatActivity(), OnLocationSelectedListener {

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
            val x = "37.5837"
            val y = "126.9999"
            val radius = 500
            val sort = "distance"

            // 검색 버튼 눌리면 키보드 숨기도록
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchLocationBtn.windowToken, 0)

            searchLocationViewModel.getLocationInfo(apiKey, query, x, y, radius, sort, object : SearchLocationRepository.GetDataCallback<LocationApiResponse> {

                override fun onSuccess(data: LocationApiResponse) {
                    binding.searchLocationRecyclerView.layoutManager = LinearLayoutManager(this@SearchLocationActivity)
                    data?.let {
                        binding.searchLocationRecyclerView.adapter = SearchLocationAdapter(it.documents, this@SearchLocationActivity)
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    // 데이터 조회 실패 시 처리
                }
            })
        }
    }

    override fun onLocationSelected(location: SearchLocationEntity) {
        val intent = Intent(this, LocationDetailActivity::class.java)
        intent.putExtra("selectedLocation", location)
        setResult(Activity.RESULT_OK, intent)
        finish()
        startActivity(intent)
    }
}

