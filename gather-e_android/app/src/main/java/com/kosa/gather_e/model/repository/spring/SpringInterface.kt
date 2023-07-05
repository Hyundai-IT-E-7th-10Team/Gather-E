package com.kosa.gather_e.model.repository.spring

import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.user.UserEntity
import com.kosa.gather_e.model.entity.map.PastMeetingGatherEntity
import com.kosa.gather_e.model.entity.user.LoginResponse
import com.kosa.gather_e.model.entity.user_gather.UserGather
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface SpringInterface {

    @GET("auth/kakao")
    fun kakaoLogin(@Query("accessToken") token: String): Call<LoginResponse>
    @GET("category")
    fun getCategory() : Call<List<CategoryEntity>>
    @POST("gather")
    fun createGather(@Body gather: GatherEntity) : Call<GatherEntity>
    @GET("gather/{gatherSeq}")
    fun getGatherDetail(@Path("gatherSeq") gatherSeq: Int) : Call<GatherEntity>
    @GET("gather/{categorySeq}/category")
    fun getGatherByCategory(@Path("categorySeq") categorySeq: Long): Call<List<GatherEntity>>
    @GET("user_gather/{gatherSeq}")
    fun getGatherUsers(@Path("gatherSeq") gatherSeq: Int) : Call<List<UserEntity>>
    @GET("gather")
    fun getGather() : Call<List<GatherEntity>>
    @GET("map/past-meeting")
    fun getPastMeetingGather() : Call<List<PastMeetingGatherEntity>>
    @POST("user_gather")
    fun joinGather(@Body gather: UserGather) : Call<UserGather>;
    @DELETE("user_gather")
    fun leaveGather(@Query("gatherSeq") gatherSeq: Long) : Call<UserGather>;
}

