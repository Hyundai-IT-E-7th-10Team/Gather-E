package com.kosa.gather_e.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.FragmentMapCurrentRecruiteBinding
import com.kosa.gather_e.databinding.FragmentMapPastMeetingBinding
import com.kosa.gather_e.databinding.ToolbarMapBinding
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.util.FusedLocationSource

lateinit var binding : FragmentMapPastMeetingBinding

class MapPastMeetingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapPastMeetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapPastMeetingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}