package com.kosa.gather_e.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.FragmentMapCurrentRecruiteBinding

class MapCurrentRecruiteFragment : Fragment() {

    lateinit var binding : FragmentMapCurrentRecruiteBinding
    private lateinit var frameLayoutMapCurrentRecruite: FrameLayout


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
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapCurrentRecruiteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}