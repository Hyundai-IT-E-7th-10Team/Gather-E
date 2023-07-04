package com.kosa.gather_e.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kosa.gather_e.WriteActivity
import com.kosa.gather_e.databinding.FragmentHomeBinding
import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.util.Categories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HomeRecyclerViewAdapter
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("kww", "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.list.observe(viewLifecycleOwner) {
            adapter = HomeRecyclerViewAdapter()
            adapter.dataList = it
            binding.homeRecycler.adapter = adapter
            binding.homeRecycler.layoutManager = LinearLayoutManager(context)
        }

        homeViewModel.isALL.observe(viewLifecycleOwner) {
            if(it) {
                binding.allOrGatheringBtn.text = "전체 보기"
            } else {
                binding.allOrGatheringBtn.text = "모집중 보기"
            }
        }

        binding.allOrGatheringBtn.setOnClickListener {
            homeViewModel.togleIsAll()
        }

        val categoryList = Categories.getCategories()
        binding.categorySpinner.adapter =
            this.context?.let {context ->
                ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    categoryList.map { it.categoryName }.toMutableList()
                )
            }
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    homeViewModel.listByCategory(categoryList[position].categorySeq.toLong())
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.writeBtn.setOnClickListener {
            val intent = Intent(activity, WriteActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("kww", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d("kww", "onStart")
        homeViewModel.refreshView()
        super.onStart()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}