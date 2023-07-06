package com.kosa.gather_e.ui.map

import GatherInfoDialogFragment
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.FragmentMapAllGatherBinding
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.util.CurrUser
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapAllGatherFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapAllGatherBinding
    private lateinit var frameLayoutMapAll : FrameLayout

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapAllGatherBinding.inflate(inflater, container, false)
        frameLayoutMapAll = binding.root.findViewById(R.id.frameLayout_map_all)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(getString(R.string.naverClientId))

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.all_map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.all_map_fragment, it).commit()
            }

        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mapFragment.getMapAsync(this)
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

    companion object {
        val LOCATION_PERMISSION_REQUEST_CODE = 1000
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

        // 코루틴을 사용하여 함수들을 순차적으로 호출합니다.
        CoroutineScope(Dispatchers.Main).launch {
            getCurrentRecruitGather()
            getPastMeetingGather()
            getRecruitedByMeGather()
        }
    }

    private suspend fun getCurrentRecruitGather() {
        // Retrofit 호출 및 응답 처리
        val response = withContext(Dispatchers.IO) {
            SpringRetrofitProvider.getRetrofit().getGather().execute()
        }
        if (response.isSuccessful) {
            val currentRecruitGatherList = response.body()?.filter { gather ->
                // 필터링 조건
                val currentDate = Calendar.getInstance().time
                val gatherDate = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(gather.gatherDate)
                gather.gatherUserCnt!! < gather.gatherLimit && gatherDate.after(currentDate)
            }
            if (currentRecruitGatherList != null) {
                for (i in currentRecruitGatherList) {
                    val marker = Marker()

                    marker.position = LatLng(
                        i.gatherLatitude,
                        i.gatherLongitude
                    )
                    marker.width = 130
                    marker.height = 130

                    when (i.categorySeq) {
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
                    marker.setOnClickListener { it ->
                        naverMap.moveCamera(CameraUpdate.scrollTo(marker.position).animate(CameraAnimation.Easing))
                        val dialog = GatherInfoDialogFragment(i)
                        dialog.show(parentFragmentManager, "GatherInfoDialogFragment")

                        true
                    }
                    marker.map = naverMap
                }
            }
        }

    }


    private suspend fun getPastMeetingGather() {
        // Retrofit 호출 및 응답 처리
        val response = withContext(Dispatchers.IO) {
            SpringRetrofitProvider.getRetrofit().getPastMeetingGather().execute()
        }
        if (response.isSuccessful) {
            val pastMeetingGatherList = response.body()

            if (pastMeetingGatherList != null) {
                for (i in pastMeetingGatherList) {
                    val marker = Marker()

                    marker.position = LatLng(
                        i.gatherLatitude,
                        i.gatherLongitude
                    )
                    marker.width = 130
                    marker.height = 130


                    when (i.categorySeq) {
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

                    marker.setOnClickListener { it ->
                        naverMap.moveCamera(CameraUpdate.scrollTo(marker.position).animate(CameraAnimation.Easing))
                        val dialog = GatherInfoDialogFragment(i)
                        dialog.show(parentFragmentManager, "GatherInfoDialogFragment")

                        true
                    }
                    marker.map = naverMap

                }
            }
        }
    }

        private suspend fun getRecruitedByMeGather() {
            // Retrofit 호출 및 응답 처리
            val response = withContext(Dispatchers.IO) {
                SpringRetrofitProvider.getRetrofit().getGather().execute()
            }

            if (response.isSuccessful) {
                val recruitedByMeGatherList = response.body()?.filter { gather ->
                    gather.gatherCreator == CurrUser.getSeq()
                }

                // 마커 생성 및 지도에 추가
                if (recruitedByMeGatherList != null) {
                    for (i in recruitedByMeGatherList) {
                        val marker = Marker()

                        marker.position = LatLng(
                            i.gatherLatitude,
                            i.gatherLongitude
                        )
                        marker.width = 130
                        marker.height = 130

                        when (i.categorySeq) {
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
                            naverMap.moveCamera(CameraUpdate.scrollTo(marker.position).animate(CameraAnimation.Easing))
                            val dialog = GatherInfoDialogFragment(i)
                            dialog.show(parentFragmentManager, "GatherInfoDialogFragment")

                            true
                        }
                        marker.map = naverMap

                    }
                }
            }
        }


    }


