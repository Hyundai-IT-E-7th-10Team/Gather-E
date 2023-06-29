package com.kosa.gather_e.model.entity.gather

data class GatherEntity(
    val categorySeq: Int,
    val categoryName: String,
    val gatherTitle: String,
    val gatherDate: String,
    val gatherDescription: String,
    val gatherLimit: Int,
    val gatherLatitude: Double,
    val gatherLongitude: Double
)
