package com.kosa.gather_e.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel() {

    private lateinit var totalList : List<GatherEntity>
    private lateinit var currList : List<GatherEntity>
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private var _list = MutableLiveData<List<GatherEntity>>().apply {
        val call = SpringRetrofitProvider.getRetrofit().getGather()
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

    val text: LiveData<String> = _text
    val list: LiveData<List<GatherEntity>> = _list

    fun listOnlyGathering() {
        _list.value = currList.filter {
            it.gatherLimit > it.gatherUserCnt!!
        }
    }
    fun listAll() {
        _list.value = currList
    }

    fun listByCategory(category: String, isAll: Boolean) {
        if (category == "전체" && isAll) {
            currList = totalList
        }
        else {
            currList = totalList.filter {
                it.categoryName == category
            }
        }
        if (isAll) {
            listAll()
        } else {
            listOnlyGathering()
        }
    }
}