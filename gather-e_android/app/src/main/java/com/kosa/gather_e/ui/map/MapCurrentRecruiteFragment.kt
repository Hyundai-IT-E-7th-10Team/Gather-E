package com.kosa.gather_e.ui.map

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.UiThread
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.FragmentMapCurrentRecruiteBinding
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapCurrentRecruiteFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding : FragmentMapCurrentRecruiteBinding
    private lateinit var frameLayoutMapCurrentRecruite: FrameLayout

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private lateinit var currentRecruitGatherList : List<GatherEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapCurrentRecruiteBinding.inflate(inflater, container, false)
        frameLayoutMapCurrentRecruite = binding.root.findViewById(R.id.frameLayout_map_current_recruite)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naverClientId))

        val fm = childFragmentManager
        val mapFragment =
            fm.findFragmentById(R.id.current_map_fragment) as com.naver.maps.map.MapFragment?
                ?: com.naver.maps.map.MapFragment.newInstance(

                )
                    .also {
                        fm.beginTransaction().add(R.id.current_map_fragment, it).commit()
                    }

        locationSource = FusedLocationSource(this, MapFragment.LOCATION_PERMISSION_REQUEST_CODE)



        mapFragment.getMapAsync (this)
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
    @SuppressLint("ResourceAsColor")
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {

        this.naverMap = naverMap
        // 처음에 현위치로 오도록
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 커스텀  버튼 누르면 현위치로 오도록
        binding.locationButton.setOnClickListener {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }

        // 줌 버튼 비활성화
        val uiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false

        // 현재 모집 중인 모임 불러와서 마커 찍기
        // 조건 : 날짜 > 현재, 모집 인원 > 현재 모인 인원
        val callGetCurrentRecruitGather: Call<List<GatherEntity>> = SpringRetrofitProvider.getRetrofit().getGather()
        callGetCurrentRecruitGather.enqueue(object : Callback<List<GatherEntity>> {
            override fun onResponse(
                call: Call<List<GatherEntity>>,
                response: Response<List<GatherEntity>>
            ) {
                Log.d("gather", "$call, $response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        currentRecruitGatherList = it
                        // 조건 필터링
                        val currentDate = Calendar.getInstance().time
                        val filteredList = currentRecruitGatherList.filter { gather ->
                            val gatherDate = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(gather.gatherDate)
                            gather.gatherUserCnt!! < gather.gatherLimit && gatherDate.after(currentDate)
                        }

                        Log.d("gather", "$currentRecruitGatherList")
                        Log.d("gather", "$filteredList")
                        val transaction = parentFragmentManager.beginTransaction()

                        for (i in filteredList.indices) {
                            val marker = Marker()

            //           marker.setOnClickListener { overlay ->
            //
            //            }
                            marker.position = LatLng(
                                currentRecruitGatherList[i].gatherLatitude,
                                currentRecruitGatherList[i].gatherLongitude
                            )
                            marker.width = 150
                            marker.height = 150


                            when (filteredList[i].categorySeq) {
                                1 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_1_football)
                                2 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_2_tennis)
                                3 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_3_golf)
                                4 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_4_basketball)
                                5 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_5_hiking)
                                6 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_6_shuttlecock)
                                7 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_7_volleyball)
                                8 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_8_bowling)
                                9 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_9_squash)
                                10 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_10_pingpong)
                                11 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_11_swimmig)
                                12 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_12_riding)
                                13 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_13_skate)
                                14 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_14_cycling)
                                15 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_15_yoga)
                                16 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_16_pilates)
                                17 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_17_climbing)
                                18 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_18_billiard)
                                19 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_19_dancing)
                                20 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_20_boxing)
                            }
                            marker.map = naverMap
                        }

                    }
                }
            }
            override fun onFailure(call: Call<List<GatherEntity>>, t: Throwable) {
                Log.d("gather", "callgetCurrentRecruitGather 실패")
            }
        })


    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

