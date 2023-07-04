package com.kosa.gather_e.ui.searchlocation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kosa.gather_e.R
import com.kosa.gather_e.WriteActivity
import com.kosa.gather_e.model.entity.location.SearchLocationEntity
import com.kosa.gather_e.databinding.ActivityLocationDetailBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityLocationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar의 X(취소) 버튼
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naverClientId))

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        // 장소 확정 버튼
        binding.confirmButton.setOnClickListener {
            val selectedLocation = intent.getSerializableExtra("selectedLocation") as SearchLocationEntity?
            Log.d("gather", "선택한 장소 : $selectedLocation")

            // WriteActivity로 다시 보내야 함
            val returnIntent = Intent(this, WriteActivity::class.java)
            returnIntent.putExtra("selectedLocation", selectedLocation)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()

        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        // Serializable보다 Parcelable 쓰는게 더 좋음
        val selectedLocation = intent.getSerializableExtra("selectedLocation") as SearchLocationEntity?
        if (selectedLocation != null) {

        val marker = Marker()
        //marker.icon = OverlayImage.fromResource(R.drawable.ic_map_mar)

        val locationLongStr = selectedLocation.x
        val locationLatStr = selectedLocation.y
        val locationLong = locationLongStr.toDoubleOrNull() ?: 0.0
        val locationLat = locationLatStr.toDoubleOrNull() ?: 0.0


        // 마커 찍은 곳으로 이동
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(locationLat, locationLong))
            .animate(CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)

        marker.position = LatLng(locationLat, locationLong)
        marker.map = naverMap

        }
    }
}