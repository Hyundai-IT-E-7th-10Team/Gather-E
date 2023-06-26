package com.kosa.gather_e

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchLocationApiService {

    @GET("https://dapi.kakao.com/v2/local/search/keyword")
    fun getLocationData(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String
    ) : Call<LocationApiResponse>
}