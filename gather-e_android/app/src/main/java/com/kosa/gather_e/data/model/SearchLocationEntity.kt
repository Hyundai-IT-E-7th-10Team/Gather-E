package com.kosa.gather_e.data.model

data class SearchLocationEntity(
    val place_name: String,
    val road_address_name: String,
    val x: String,
    val y: String,
    val distance: String,
    val phone: String,
    val place_url: String
)

data class LocationApiResponse(
    val documents: List<SearchLocationEntity>
)