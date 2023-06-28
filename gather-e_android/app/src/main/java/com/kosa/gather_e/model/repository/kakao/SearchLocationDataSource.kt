package com.kosa.gather_e.model.repository.kakao


import com.kosa.gather_e.model.entity.location.LocationApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchLocationDataSource {

    fun getLocationInfo(apiKey: String,
                        query: String,
                        x: String,
                        y: String,
                        radius: Int,
                        sort: String,
                        callback: SearchLocationRepository.GetDataCallback<LocationApiResponse>
    ) {

        val locationCall = KakaoRetrofitProvider.getRetrofit().getLocationData(apiKey, query, x, y, radius, sort)

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
