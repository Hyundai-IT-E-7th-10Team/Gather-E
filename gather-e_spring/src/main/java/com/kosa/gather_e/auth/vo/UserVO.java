package com.kosa.gather_e.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserVO {
    long userSeq;
    String userEmail;
    String userName;
    String userNickname;
    String userProfileImg;
}
