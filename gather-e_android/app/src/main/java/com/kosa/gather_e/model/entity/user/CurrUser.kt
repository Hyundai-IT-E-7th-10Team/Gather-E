package com.kosa.gather_e.model.entity.user

import com.kakao.sdk.user.model.User

object CurrUser {
    private var name: String = ""
    private var profileImgUrl: String = ""

    fun getUserName(): String {
        return name
    }

    fun getProfileImgUrl(): String {
        return profileImgUrl
    }

    fun setCurrUser(user: User) {
        this.name = user.kakaoAccount?.profile?.nickname.toString()
        this.profileImgUrl = user.kakaoAccount?.profile?.thumbnailImageUrl.toString()
    }
}