package com.kosa.gather_e.model.entity.user

import com.google.gson.annotations.SerializedName

data class UserEntity(
    @SerializedName("userSeq")
    val userSeq: Long,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userProfileImg")
    val userProfileImg: String
)
