package com.kosa.gather_e.util

import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.user.model.User
import com.kosa.gather_e.model.entity.user.UserEntity

object CurrUser {
    private var name: String = ""
    private var profileImgUrl: String = ""
    private var seq: Long = 0
    private var token:String =""

    fun getUserName(): String {
        return name
    }

    fun getProfileImgUrl(): String {
        return profileImgUrl
    }

    fun setCurrUser(user: UserEntity) {
        name = user.userName
        profileImgUrl = user.userProfileImg
        seq = user.userSeq
    }

    fun setToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    token = task.result
                }
            }
    }

    fun getToken(): String{
        return token
    }

    fun getSeq() : Long {
        return seq
    }
}