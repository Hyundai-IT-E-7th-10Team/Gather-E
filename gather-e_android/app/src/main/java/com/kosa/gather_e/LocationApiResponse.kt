package com.kosa.gather_e

data class Location(
    val place_name: String,
    val road_address_name: String,
    val x: String,
    val y: String,
    val distance: String
)

data class LocationApiResponse(
    val documents: List<Location>
)