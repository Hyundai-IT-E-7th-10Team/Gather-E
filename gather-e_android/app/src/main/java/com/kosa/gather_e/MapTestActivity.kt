package com.kosa.gather_e

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kosa.gather_e.databinding.ActivityMapTestBinding

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class MapTestActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityMapTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("c3cnfa0btg")

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(naverMap: NaverMap) {
        val uiSettings = naverMap.uiSettings
        // 현위치 버튼
        //uiSettings.isLocationButtonEnabled = true
        // 빨간색 표시 마커 (네이버맵 현재 가운데에 항상 위치)
        val marker = Marker()
        marker.position = LatLng(
            naverMap.cameraPosition.target.latitude,
            naverMap.cameraPosition.target.longitude
        )
        marker.icon = OverlayImage.fromResource(R.drawable.ic_map_pin)
        marker.map = naverMap



    }

}
