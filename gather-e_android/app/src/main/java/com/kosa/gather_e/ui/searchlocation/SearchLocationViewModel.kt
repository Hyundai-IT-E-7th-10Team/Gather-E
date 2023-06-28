package com.kosa.gather_e.ui.searchlocation

import androidx.lifecycle.ViewModel
import com.kosa.gather_e.model.entity.location.LocationApiResponse
import com.kosa.gather_e.model.repository.kakao.SearchLocationDataSource
import com.kosa.gather_e.model.repository.kakao.SearchLocationRepository


// ViewModel()를 상속받지 않고 구현했음
class SearchLocationViewModel : ViewModel() {

    fun getLocationInfo(apiKey: String,
                        query: String,
                        x: String,
                        y: String,
                        radius: Int,
                        sort: String,
                        callback: SearchLocationRepository.GetDataCallback<LocationApiResponse>) {
        SearchLocationDataSource.getLocationInfo(apiKey, query, x, y, radius, sort, callback)
    }

}
