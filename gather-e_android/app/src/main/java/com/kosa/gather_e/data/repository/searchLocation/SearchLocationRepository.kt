package com.kosa.gather_e.data.repository.searchLocation

import com.kosa.gather_e.data.model.LocationApiResponse

// DataSource로 부터 Model을 가져오는 것을 추상화하는 역할

object SearchLocationRepository {

    private val searchLocationDataSource = SearchLocationDataSource

    fun getLocationInfo(apiKey: String,
                        query: String,
                        x: String,
                        y: String,
                        radius: Int,
                        sort: String,
                        callback: GetDataCallback<LocationApiResponse>){
        searchLocationDataSource.getLocationInfo(apiKey, query, x, y, radius, sort, callback)
    }

    // 데이터 조회 콜백
    interface GetDataCallback<T> {
        fun onSuccess(data: LocationApiResponse)
        fun onFailure(throwable: Throwable)
    }
}