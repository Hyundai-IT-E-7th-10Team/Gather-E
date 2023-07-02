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
import com.kosa.gather_e.model.entity.map.CurrentRecruitGatherEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var toolbarBinding: ToolbarMapBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var currentRecruitGatherList : List<CurrentRecruitGatherEntity>;

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

        // 초기화 버튼
        toolbarBinding.actionNavigationMapToMapButton1.setOnClickListener {
            val mapFragment = MapFragment()
            parentFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.map_frame_container, mapFragment) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
            .addToBackStack(null)
            .commit()
        }

        // 현재 모집 중 버튼
        toolbarBinding.actionNavigationMapToMapButton2.setOnClickListener {
            val mapCurrentRecruiteFragment = MapCurrentRecruiteFragment()
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.map_frame_container, mapCurrentRecruiteFragment) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
                .addToBackStack(null)
                .commit()
        }

        // 참여했던 모임 추억 버튼
        toolbarBinding.actionNavigationMapToMapButton3.setOnClickListener {
            val mapPastMeetingFragment = MapPastMeetingFragment()
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.map_frame_container, mapPastMeetingFragment) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
                .addToBackStack(null)
                .commit()
        }

        // 내가 주최한 모임 버튼
        toolbarBinding.actionNavigationMapToMapButton4.setOnClickListener {
            val mapFragment = MapFragment()
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.map_frame_container, mapFragment) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
                .addToBackStack(null)
                .commit()
        }

        //
        val callgetCurrentRecruitGather: Call<List<CurrentRecruitGatherEntity>> = SpringRetrofitProvider.getRetrofit().getCurrentRecruitGather()
        callgetCurrentRecruitGather.enqueue(object : Callback<List<CurrentRecruitGatherEntity>> {
            override fun onResponse(
                call: Call<List<CurrentRecruitGatherEntity>>,
                response: Response<List<CurrentRecruitGatherEntity>>
            ) {
                Log.d("gather", "$call, $response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        currentRecruitGatherList = it
                    }
                }
            }
            override fun onFailure(call: Call<List<CurrentRecruitGatherEntity>>, t: Throwable) {
                Log.d("gather", "callgetCurrentRecruitGather 실패")
            }
        })
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

        val transaction = parentFragmentManager.beginTransaction()

        for (i in 0..currentRecruitGatherList.size - 1) {
            val marker = Marker()
//            marker.setOnClickListener { overlay ->
//
//            }
            marker.position = LatLng(
                currentRecruitGatherList[i].gatherLatitude,
                currentRecruitGatherList[i].gatherLongitude
            )
            marker.width = 100
            marker.height = 110
            when (currentRecruitGatherList[i].categorySeq) {
                1 -> OverlayImage.fromResource(R.drawable.ic_1_football)
                2 -> OverlayImage.fromResource(R.drawable.ic_2_tennis)
                3 -> OverlayImage.fromResource(R.drawable.ic_3_golf)
                4 -> OverlayImage.fromResource(R.drawable.ic_4_basketball)
                5 -> OverlayImage.fromResource(R.drawable.ic_5_hiking)
                6 -> OverlayImage.fromResource(R.drawable.ic_6_shuttlecock)
                7 -> OverlayImage.fromResource(R.drawable.ic_7_volleyball)
                8 -> OverlayImage.fromResource(R.drawable.ic_8_bowling)
                9 -> OverlayImage.fromResource(R.drawable.ic_9_squash)
                10 -> OverlayImage.fromResource(R.drawable.ic_10_pingpong)
                11 -> OverlayImage.fromResource(R.drawable.ic_11_swimmig)
                12 -> OverlayImage.fromResource(R.drawable.ic_12_riding)
                13 -> OverlayImage.fromResource(R.drawable.ic_13_skate)
                14 -> OverlayImage.fromResource(R.drawable.ic_14_cycling)
                15 -> OverlayImage.fromResource(R.drawable.ic_15_yoga)
                16 -> OverlayImage.fromResource(R.drawable.ic_16_pilates)
                17 -> OverlayImage.fromResource(R.drawable.ic_17_climbing)
                18 -> OverlayImage.fromResource(R.drawable.ic_18_billiard)
                19 -> OverlayImage.fromResource(R.drawable.ic_19_dancing)
                20 -> OverlayImage.fromResource(R.drawable.ic_20_boxing)
            }
            marker.map = naverMap
        }
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

