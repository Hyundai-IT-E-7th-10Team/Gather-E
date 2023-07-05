package com.kosa.gather_e.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    UserVO user;
    String accessToken;
}
