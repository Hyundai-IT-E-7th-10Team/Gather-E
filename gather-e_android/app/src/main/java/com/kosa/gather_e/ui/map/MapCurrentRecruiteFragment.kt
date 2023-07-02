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
import com.kosa.gather_e.model.entity.map.CurrentRecruitGatherEntity
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

    private lateinit var currentRecruitGatherList : List<CurrentRecruitGatherEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

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
        val mapFragment = fm.findFragmentById(R.id.current_map_fragment) as com.naver.maps.map.MapFragment?
            ?: com.naver.maps.map.MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.current_map_fragment, it).commit()
            }

        locationSource =
            FusedLocationSource(this, MapFragment.LOCATION_PERMISSION_REQUEST_CODE)

        mapFragment.getMapAsync(this)
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
                        Log.d("gather", "$currentRecruitGatherList")
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
    @SuppressLint("ResourceAsColor")
    @UiThread
    override fun onMapReady(p0: NaverMap) {
        val transaction = parentFragmentManager.beginTransaction()

        for (i in currentRecruitGatherList.indices) {
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


            when (currentRecruitGatherList[i].categorySeq) {
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

            marker.iconTintColor = R.color.purple
            marker.alpha = 1f
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

