package com.kosa.gather_e.ui.map

import GatherInfoDialogFragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.UiThread
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.FragmentMapPastMeetingBinding
import com.kosa.gather_e.databinding.FragmentMapRecruitedByMeBinding
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.util.CurrUser
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
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

class MapRecruitedByMeFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding : FragmentMapRecruitedByMeBinding
    private lateinit var frameLayoutMapRecruitedByMeFragment: FrameLayout

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private lateinit var recruitedByMeGatherList : List<GatherEntity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapRecruitedByMeBinding.inflate(inflater, container, false)
        frameLayoutMapRecruitedByMeFragment = binding.root.findViewById(R.id.frameLayout_map_recruited_by_me)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naverClientId))

        val fm = childFragmentManager
        val mapFragment =
            fm.findFragmentById(R.id.by_me_map_fragment) as com.naver.maps.map.MapFragment?
                ?: com.naver.maps.map.MapFragment.newInstance(

                )
                    .also {
                        fm.beginTransaction().add(R.id.by_me_map_fragment, it).commit()
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

        // 내가 주최한 모임 불러와서 마커 찍기
        // 조건 : 모임 생성자 = 로그인된 사용자
        val callGetRecruitedByMeGather: Call<List<GatherEntity>> = SpringRetrofitProvider.getRetrofit().getGather()
        callGetRecruitedByMeGather.enqueue(object : Callback<List<GatherEntity>>{
            override fun onResponse(
                call: Call<List<GatherEntity>>,
                response: Response<List<GatherEntity>>
            ) {
                Log.d("gather", "$call, $response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        recruitedByMeGatherList = it
                        // 조건 필터링
                        val filteredList = recruitedByMeGatherList.filter { gather ->
                            gather.gatherCreator == CurrUser.getSeq()
                        }

                        Log.d("gather", "$recruitedByMeGatherList")
                        Log.d("gather", "$filteredList")
                        val transaction = parentFragmentManager.beginTransaction()

                        for (i in filteredList.indices) {
                            val marker = Marker()

                            marker.position = LatLng(
                                filteredList[i].gatherLatitude,
                                filteredList[i].gatherLongitude
                            )

                            marker.width = 150
                            marker.height = 150


                            when (filteredList[i].categorySeq) {
                                1 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m1_football)
                                2 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m2_tennis)
                                3 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m3_golf)
                                4 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m4_basketball)
                                5 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m5_hiking)
                                6 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m6_shuttlecock)
                                7 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m7_volleyball)
                                8 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m8_bowling)
                                9 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m9_squash)
                                10 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m10_pingpong)
                                11 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m11_swimmig)
                                12 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m12_riding)
                                13 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m13_skate)
                                14 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m14_cycling)
                                15 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m15_yoga)
                                16 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m16_pilates)
                                17 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m17_climbing)
                                18 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m18_billiard)
                                19 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m19_dancing)
                                20 -> marker.icon = OverlayImage.fromResource(R.drawable.ic_m20_boxing)
                            }

                            marker.setOnClickListener { it ->
                                naverMap.moveCamera(
                                    CameraUpdate.scrollTo(marker.position).animate(
                                        CameraAnimation.Easing))
                                val dialog = GatherInfoDialogFragment(filteredList[i])
                                dialog.show(parentFragmentManager, "GatherInfoDialogFragment")

                                true
                            }


                            marker.map = naverMap

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<GatherEntity>>, t: Throwable) {
                Log.d("gather", "callGetRecruitedByMeGather 실패")
            }

        })

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}