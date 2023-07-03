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
import com.kosa.gather_e.R
import com.kosa.gather_e.WriteActivity
import com.kosa.gather_e.databinding.FragmentHomeBinding
import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter:HomeRecyclerViewAdapter
    private var isAll = true;

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
        binding.allOrGatheringBtn.setOnClickListener{
            if(isAll) {
                binding.allOrGatheringBtn.text = "모집중 보기"
                homeViewModel.listOnlyGathering()
                isAll = false
            }
            else {
                binding.allOrGatheringBtn.text = "전체 보기"
                homeViewModel.listAll()
                isAll = true
            }
        }
        val call = SpringRetrofitProvider.getRetrofit().getCategory()
        call.enqueue(object : Callback<List<CategoryEntity>> {
            override fun onResponse(
                call: Call<List<CategoryEntity>>,
                response: Response<List<CategoryEntity>>
            ) {
                val res = (response.body()!!.map { it.categoryName }).toMutableList()
                res.add(0, "전체")
                binding.categorySpinner.adapter = context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1, res) }
                binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        homeViewModel.listByCategory(res[position], isAll)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
            override fun onFailure(call: Call<List<CategoryEntity>>, t: Throwable) {
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.writeBtn.setOnClickListener {
            val intent = Intent(activity, WriteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}