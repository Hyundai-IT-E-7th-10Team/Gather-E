package com.kosa.gather_e.ui.searchlocation

import androidx.lifecycle.ViewModel
import com.kosa.gather_e.data.model.LocationApiResponse
import com.kosa.gather_e.data.repository.searchLocation.SearchLocationDataSource
import com.kosa.gather_e.data.repository.searchLocation.SearchLocationRepository


// ViewModel()를 상속받지 않고 구현했음
class SearchLocationViewModel : ViewModel() {

    fun getLocationInfo(apiKey: String, query: String, callback: SearchLocationRepository.GetDataCallback<LocationApiResponse>) {
        SearchLocationDataSource.getLocationInfo(apiKey, query, callback)
    }

}
