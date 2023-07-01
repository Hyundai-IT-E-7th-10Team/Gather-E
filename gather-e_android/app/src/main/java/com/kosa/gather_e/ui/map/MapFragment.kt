package com.kosa.gather_e.ui.map

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.FragmentMapBinding
import com.kosa.gather_e.databinding.ToolbarMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var toolbarBinding: ToolbarMapBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        toolbarBinding = ToolbarMapBinding.bind(binding.root.findViewById(R.id.toolbar_map_layout))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naverClientId))

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mapFragment.getMapAsync(this)

/*        // 초기화 버튼
        toolbarBinding.mapButton1.setOnClickListener {
            parentFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_map, mapFragment) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
            .addToBackStack(null)
            .commit()
        }*/

        // 현재 모집 중 버튼
        toolbarBinding.actionNavigationMapToMapButton2.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_map_to_map_button2)
        }

        // 참여했던 모임 추억
        toolbarBinding.mapButton3.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_map_to_map_button3)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true


        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow


//        val uiSettings = naverMap.uiSettings
//        // 현위치 버튼
//        uiSettings.isLocationButtonEnabled = true
//
//        // 마커 위도경도로 띄우기
//        val marker = Marker()
//        marker.position = LatLng(37.5670135, 126.9783740)
//        marker.map = naverMap
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
