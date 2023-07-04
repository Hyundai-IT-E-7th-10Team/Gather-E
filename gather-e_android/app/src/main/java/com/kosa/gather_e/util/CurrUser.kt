package com.kosa.gather_e.util

import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.user.model.User

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

    fun setCurrUser(user: User) {
        name = user.kakaoAccount?.profile?.nickname.toString()
        profileImgUrl = user.kakaoAccount?.profile?.thumbnailImageUrl.toString()
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

    fun setSeq(seq: Long){
        CurrUser.seq = seq
    }
    fun getSeq() : Long {
        return seq
    }
}