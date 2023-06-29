package com.kosa.gather_e.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient

class MypageViewModel : ViewModel() {

    private val _name = MutableLiveData<String>().apply {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                value = user.kakaoAccount?.profile?.nickname
            }
        }
    }

    private val _profileImg = MutableLiveData<String>().apply {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                value = user.kakaoAccount?.profile?.thumbnailImageUrl
            }
        }
    }


    val name: LiveData<String> = _name
    val profileImg: LiveData<String> = _profileImg
}