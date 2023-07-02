package com.kosa.gather_e.model.entity.gather

data class GatherInfo(
    val status: String,
    val title: String,
    val location: String,
    val creatorName: String,
    val creatorImg: String,
    val time: String,
    val maxNum: Int,
    val currNum: Int
)