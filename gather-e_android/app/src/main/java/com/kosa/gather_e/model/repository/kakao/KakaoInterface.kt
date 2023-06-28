package com.kosa.gather_e.model.repository.kakao

import com.kosa.gather_e.model.entity.location.LocationApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface KakaoInterface {

    @GET("https://dapi.kakao.com/v2/local/search/keyword")
    fun getLocationData(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int,
        @Query("sort") sort: String
    ) : Call<LocationApiResponse>


}