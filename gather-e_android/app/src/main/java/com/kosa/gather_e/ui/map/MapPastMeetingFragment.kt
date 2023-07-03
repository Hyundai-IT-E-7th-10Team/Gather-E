package com.kosa.gather_e.ui.map

import android.annotation.SuppressLint
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
import com.kosa.gather_e.databinding.FragmentMapPastMeetingBinding
import com.kosa.gather_e.model.entity.map.PastMeetingGatherEntity
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

class MapPastMeetingFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding : FragmentMapPastMeetingBinding
    private lateinit var frameLayoutMapPastMeetingFragment: FrameLayout

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private lateinit var pastMeetingGatherList : List<PastMeetingGatherEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapPastMeetingBinding.inflate(inflater, container, false)
        frameLayoutMapPastMeetingFragment = binding.root.findViewById(R.id.frameLayout_map_past_meeting)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naverClientId))

        val fm = childFragmentManager
        val mapFragment =
            fm.findFragmentById(R.id.past_map_fragment) as com.naver.maps.map.MapFragment?
                ?: com.naver.maps.map.MapFragment.newInstance(

                )
                    .also {
                        fm.beginTransaction().add(R.id.past_map_fragment, it).commit()
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

        // 내가 참여했던 모임 불러와서 마커 찍기
        val callGetPastMeetingGather: Call<List<PastMeetingGatherEntity>> = SpringRetrofitProvider.getRetrofit().getPastMeetingGather()
        callGetPastMeetingGather.enqueue(object : Callback<List<PastMeetingGatherEntity>> {
            override fun onResponse(
                call: Call<List<PastMeetingGatherEntity>>,
                response: Response<List<PastMeetingGatherEntity>>
            ) {
                Log.d("gather", "$call, $response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        pastMeetingGatherList = it
                        Log.d("gather", "$pastMeetingGatherList")
                        val transaction = parentFragmentManager.beginTransaction()

                        for (i in pastMeetingGatherList.indices) {
                            val marker = Marker()

                            //           marker.setOnClickListener { overlay ->
                            //
                            //            }
                            marker.position = LatLng(
                                pastMeetingGatherList[i].gatherLatitude,
                                pastMeetingGatherList[i].gatherLongitude
                            )
                            marker.width = 150
                            marker.height = 150


                            when (pastMeetingGatherList[i].categorySeq) {
                                1 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p1_football)
                                2 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p2_tennis)
                                3 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p3_golf)
                                4 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p4_basketball)
                                5 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p5_hiking)
                                6 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p6_shuttlecock)
                                7 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p7_volleyball)
                                8 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p8_bowling)
                                9 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p9_squash)
                                10 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p10_pingpong)
                                11 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p11_swimmig)
                                12 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p12_riding)
                                13 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p13_skate)
                                14 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p14_cycling)
                                15 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p15_yoga)
                                16 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p16_pilates)
                                17 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p17_climbing)
                                18 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p18_billiard)
                                19 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p19_dancing)
                                20 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_p20_boxing)
                            }
                            marker.map = naverMap
                        }

                    }
                }
            }
            override fun onFailure(call: Call<List<PastMeetingGatherEntity>>, t: Throwable) {
                Log.d("gather", "callGetPastMeetingGather 실패")
            }
        })


    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

