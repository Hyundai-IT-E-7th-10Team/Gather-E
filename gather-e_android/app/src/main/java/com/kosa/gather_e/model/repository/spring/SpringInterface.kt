package com.kosa.gather_e.model.repository.spring

import com.kosa.gather_e.model.entity.user.JwtToken
import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.map.CurrentRecruitGatherEntity
import com.kosa.gather_e.model.entity.user.UserEntity
import com.kosa.gather_e.model.entity.map.PastMeetingGatherEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface SpringInterface {

    @GET("auth/kakao")
    fun login(@Query("accessToken") token: String): Call<JwtToken>
    @GET("category")
    fun getCategory() : Call<List<CategoryEntity>>
    @POST("gather")
    fun createGather(@Body gather: GatherEntity) : Call<GatherEntity>
    @GET("map/currentrecruit")
    fun getCurrentRecruitGather() : Call<List<CurrentRecruitGatherEntity>>
    @GET("gather/{gatherSeq}")
    fun getGatherDetail(@Path("gatherSeq") gatherSeq: Int) : Call<GatherEntity>
    @GET("user_gather/{gatherSeq}")
    fun getGatherUsers(@Path("gatherSeq") gatherSeq: Int) : Call<List<UserEntity>>
    @GET("gather")
    fun getGather() : Call<List<GatherEntity>>
    @GET("map/past-meeting")
    fun getPastMeetingGather() : Call<List<PastMeetingGatherEntity>>
}

