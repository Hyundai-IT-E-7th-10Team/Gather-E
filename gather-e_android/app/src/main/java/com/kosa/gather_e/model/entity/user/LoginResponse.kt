package com.kosa.gather_e.model.entity.user

data class LoginResponse(
    val accessToken: String,
    val user: UserEntity
)