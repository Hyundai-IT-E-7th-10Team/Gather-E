package com.kosa.gather_e.model.repository.spring

import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.entity.gather.GatherEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface SpringInterface {

    @GET("auth/kakao")
    fun login(@Query("accessToken") token: String): Call<String>
    @GET("category")
    fun getCategory() : Call<List<CategoryEntity>>
    @POST("gather")
    fun createGather(@Body gather: GatherEntity) : Call<GatherEntity>
    @GET("gather")
    fun getGather() : Call<List<GatherEntity>>


}