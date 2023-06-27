package com.kosa.gather_e.auth.dto;

import com.kosa.gather_e.auth.vo.UserVO;
import lombok.Data;

@Data
public class KakaoUserResponse {

    KakaoAccount kakao_account;

    public UserVO toUserVO() {
        return UserVO.builder()
                .userEmail(this.kakao_account.email)
                .userName(this.kakao_account.profile.nickname)
                .userNickname(this.kakao_account.profile.nickname)
                .userProfileImg(this.kakao_account.profile.profile_image_url)
                .build();
    }
}
