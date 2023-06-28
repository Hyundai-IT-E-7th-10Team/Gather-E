package com.kosa.gather_e.data.repository.searchLocation


import com.kosa.gather_e.data.model.LocationApiResponse
import com.kosa.gather_e.data.model.SearchLocationEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchLocationDataSource {
    // 카카오 지도 검색 API
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val locationApiService = retrofit.create(SearchLocationAPIService::class.java)

    fun getLocationInfo(apiKey: String,
                        query: String,
                        x: String,
                        y: String,
                        radius: Int,
                        sort: String,
                        callback: SearchLocationRepository.GetDataCallback<LocationApiResponse>) {

        val locationCall = locationApiService.getLocationData(apiKey, query, x, y, radius, sort)

        locationCall.enqueue(object : Callback<LocationApiResponse> {
            override fun onResponse(call: Call<LocationApiResponse>, response: Response<LocationApiResponse>) {
                if (response.isSuccessful) {
                    val locationData = response.body()?.documents ?: emptyList()
                    callback.onSuccess(LocationApiResponse(locationData))
                } else {
                    callback.onFailure(Throwable("Response is not successful"))
                }
            }

            override fun onFailure(call: Call<LocationApiResponse>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }


}
