package com.kosa.gather_e.ui.searchlocation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kosa.gather_e.R
import com.kosa.gather_e.model.entity.location.LocationApiResponse
import com.kosa.gather_e.model.entity.location.SearchLocationEntity
import com.kosa.gather_e.model.repository.kakao.SearchLocationRepository
import com.kosa.gather_e.databinding.ActivitySearchLocationBinding

class SearchLocationActivity : AppCompatActivity(), OnLocationSelectedListener {

    private val searchLocationViewModel = SearchLocationViewModel()
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private lateinit var binding: ActivitySearchLocationBinding
    private lateinit var apiKey: String
    private lateinit var query: String
    private var radius: Int = 20000
    private var sort: String = "distance"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar의 X(취소) 버튼
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        // 검색 버튼
        binding.searchLocationBtn.setOnClickListener {
            apiKey = getString(R.string.kakaoRestApiKey)
            query = binding.searchLocationEdittext.text.toString()

            // 위치 권한이 있는지 확인합니다.
            if (checkLocationPermission()) {
                // 위치 권한이 있으면 현재 위치를 가져옵니다.
                getCurrentLocation()
            } else {
                // 위치 권한을 요청합니다.
                requestLocationPermission()
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun getCurrentLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val provider = locationManager.getBestProvider(criteria, true)

        if (provider != null && checkLocationPermission()) {
            val location = locationManager.getLastKnownLocation(provider)
            location?.let {
                val x = it.latitude.toString()
                val y = it.longitude.toString()

                // 검색 버튼 눌리면 키보드 숨기도록
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchLocationBtn.windowToken, 0)

                searchLocationViewModel.getLocationInfo(
                    apiKey,
                    query,
                    x,
                    y,
                    radius,
                    sort,
                    object : SearchLocationRepository.GetDataCallback<LocationApiResponse> {
                        override fun onSuccess(data: LocationApiResponse) {
                            binding.searchLocationRecyclerView.layoutManager =
                                LinearLayoutManager(this@SearchLocationActivity)
                            data?.let {
                                binding.searchLocationRecyclerView.adapter =
                                    SearchLocationAdapter(
                                        it.documents,
                                        this@SearchLocationActivity
                                    )
                            }
                        }

                        override fun onFailure(throwable: Throwable) {
                            // 데이터 조회 실패 시 처리
                        }
                    })
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 허용된 경우 현재 위치를 가져옵니다.
                getCurrentLocation()
            } else {
                // 위치 권한이 거부된 경우 처리
            }
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
