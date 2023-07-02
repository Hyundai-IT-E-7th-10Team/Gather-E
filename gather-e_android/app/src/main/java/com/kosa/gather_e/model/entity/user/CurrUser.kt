package com.kosa.gather_e.model.entity.user

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.user.model.User

object CurrUser {
    private var name: String = ""
    private var profileImgUrl: String = ""
    private var token:String =""

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

    fun setToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    this.token = task.result
                }
            }
    }

    fun getToken(): String{
        return token
    }
}