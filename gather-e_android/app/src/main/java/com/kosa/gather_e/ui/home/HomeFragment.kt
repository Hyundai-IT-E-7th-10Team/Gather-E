package com.kosa.gather_e.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.kosa.gather_e.R
import com.kosa.gather_e.WriteActivity
import com.kosa.gather_e.databinding.ActivityMainBinding
import com.kosa.gather_e.databinding.FragmentHomeBinding
import com.kosa.gather_e.model.entity.gather.GatherInfo
import com.kosa.gather_e.model.entity.user.CurrUser

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter:HomeRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.list.observe(viewLifecycleOwner) {
            adapter=HomeRecyclerViewAdapter()
            adapter.dataList = it
            binding.homeRecycler.adapter = adapter
            binding.homeRecycler.layoutManager = LinearLayoutManager(context)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 버튼 클릭 리스너를 설정합니다.
        binding.writeBtn.setOnClickListener {
            // 새로운 Intent 객체를 만들어 WriteActivity를 시작합니다.
            val intent = Intent(activity, WriteActivity::class.java)
            startActivity(intent)
            // 슬라이드 업 애니메이션을 적용합니다. 이를 위해서는 먼저 res/anim 폴더에 slide_up_animation.xml 파일을 만들어야 합니다.
            activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}