package com.kosa.gather_e.model.entity.gather

import com.kosa.gather_e.model.entity.user.UserEntity
import java.io.Serializable

data class GatherEntity(
    var gatherSeq: Int?,
    var categorySeq: Int,
    var categoryName: String,
    var gatherCreator: Int,
    var creatorName: String?,
    var creatorImgUrl: String?,
    var gatherTitle: String,
    var gatherDate: String,
    var gatherDescription: String,
    var gatherLimit: Int,
    var gatherUserCnt: Int?,
    var gatherLatitude: Double,
    var gatherLongitude: Double,
    var gatherLocationName: String
) : Serializable
