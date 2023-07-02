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
                value = response.body()
            }
        })

    }

    val text: LiveData<String> = _text
    val list: LiveData<List<GatherEntity>> = _list
}