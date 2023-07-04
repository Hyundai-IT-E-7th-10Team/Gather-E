package com.kosa.gather_e.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.util.GatherUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel() {

    private lateinit var totalList: List<GatherEntity>

    private var _isALL = MutableLiveData<Boolean>().apply {
        value = true
    }
    private var _category = MutableLiveData<Long>().apply {
        value = 1
    }
    private var _list = MutableLiveData<List<GatherEntity>>().apply {
        val call = SpringRetrofitProvider.getRetrofit().getGatherByCategory(1)
        call.enqueue(object : Callback<List<GatherEntity>> {
            override fun onFailure(call: Call<List<GatherEntity>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<GatherEntity>>,
                response: Response<List<GatherEntity>>
            ) {
                Log.d("gather", response.body().toString())
                totalList = response.body()!!
                value = response.body()
            }
        })
    }

    val list: LiveData<List<GatherEntity>> = _list
    val isALL: LiveData<Boolean> = _isALL
    val category: LiveData<Long> = _category

    fun listByCategory(categorySeq: Long) {
        _category.value = categorySeq
        refreshView()
    }

    fun togleIsAll() {
        _isALL.value = !isALL.value!!
        changeListView()
    }
    fun refreshView() {
        SpringRetrofitProvider.getRetrofit().getGatherByCategory(category.value!!).enqueue(
            object : Callback<List<GatherEntity>> {
                override fun onResponse(
                    call: Call<List<GatherEntity>>,
                    response: Response<List<GatherEntity>>
                ) {
                    totalList = response.body()!!
                    changeListView()
                }
                override fun onFailure(call: Call<List<GatherEntity>>, t: Throwable) {
                }

            }
        )
    }
    fun changeListView() {
        if (isALL.value!!) {
            _list.value = totalList
        } else {
            _list.value = totalList.filter {
                GatherUtil.isGathering(it) && !GatherUtil.isFull(it)
            }
        }
    }
}