package com.kosa.gather_e.model.entity.user

import com.google.gson.annotations.SerializedName

data class JwtToken(
    @SerializedName("accessToken")
    val accessToken: String,
    val userSeq: Long
)