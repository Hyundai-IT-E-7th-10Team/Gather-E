package com.kosa.gather_e.model.entity.map

data class PastMeetingGatherEntity(

    var categorySeq: Int,
    var gatherTitle: String,
    var gatherDate: String,
    var gatherDescription: String,
    var gatherLimit: Int,
    var gatherLatitude: Double,
    var gatherLongitude: Double,
    var gatherLocationName: String,

    var userSeq : Long
)
