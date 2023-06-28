package com.kosa.gather_e.model.entity.location

import java.io.Serializable

data class SearchLocationEntity(
    val place_name: String,
    val road_address_name: String,
    val x: String,
    val y: String,
    val distance: String,
    val phone: String,
    val place_url: String
) : Serializable // 객체를 직렬화 하여 Intent로 전달할 수 있게 함!

data class LocationApiResponse(
    val documents: List<SearchLocationEntity>
)