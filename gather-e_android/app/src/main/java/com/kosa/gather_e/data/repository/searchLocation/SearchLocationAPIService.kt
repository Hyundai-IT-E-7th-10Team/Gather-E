package com.kosa.gather_e.data.repository.searchLocation

import com.kosa.gather_e.data.model.LocationApiResponse
import com.kosa.gather_e.data.model.SearchLocationEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchLocationAPIService {

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