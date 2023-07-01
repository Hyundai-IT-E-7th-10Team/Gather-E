package com.kosa.gather_e.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.model.entity.user.CurrUser

class MypageViewModel : ViewModel() {

    private val _name = MutableLiveData<String>().apply {
        value = CurrUser.getUserName()
    }

    private val _profileImg = MutableLiveData<String>().apply {
        value = CurrUser.getProfileImgUrl()
    }

    val name: LiveData<String> = _name
    val profileImg: LiveData<String> = _profileImg
}