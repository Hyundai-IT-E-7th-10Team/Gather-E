package com.kosa.gather_e.ui.map

import GatherInfoDialogFragment
import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.FragmentMapBinding
import com.kosa.gather_e.databinding.ToolbarMapBinding
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var toolbarBinding: ToolbarMapBinding


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

        // 처음 화면에 MapAllGatherFragment를 보여주기
        val initialFragment = MapAllGatherFragment()
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.map_frame_container,
                initialFragment
            )
            .commit()

        // 초기화 버튼
        toolbarBinding.actionNavigationMapToMapButton1.setOnClickListener {


            val mapAllGatherFragment = MapAllGatherFragment()
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.map_frame_container,
                    mapAllGatherFragment
                ) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
                .addToBackStack(null)
                .commit()
        }

        // 현재 모집 중 버튼
        toolbarBinding.actionNavigationMapToMapButton2.setOnClickListener {
            val mapCurrentRecruiteFragment = MapCurrentRecruiteFragment()
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.map_frame_container,
                    mapCurrentRecruiteFragment
                ) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
                .addToBackStack(null)
                .commit()
        }

        // 참여했던 모임 추억 버튼
        toolbarBinding.actionNavigationMapToMapButton3.setOnClickListener {
            val mapPastMeetingFragment = MapPastMeetingFragment()
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.map_frame_container,
                    mapPastMeetingFragment
                ) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
                .addToBackStack(null)
                .commit()
        }

        // 내가 주최한 모임 버튼
        toolbarBinding.actionNavigationMapToMapButton4.setOnClickListener {
            val mapRecruitedByMeFragment = MapRecruitedByMeFragment()
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.map_frame_container,
                    mapRecruitedByMeFragment
                ) //Fragment 트랜잭션의 백 스택 작업을 원자적인 작업(한번에 하나의 트랜잭션만 가능)으로 설정
                .addToBackStack(null)
                .commit()
        }
    }


    companion object {
        val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {



    }

}

