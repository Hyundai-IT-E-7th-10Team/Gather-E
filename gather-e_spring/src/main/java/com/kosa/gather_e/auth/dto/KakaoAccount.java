package com.kosa.gather_e.auth.dto;

import lombok.Data;

@Data
public class KakaoAccount {
    KakaoProfile profile;
    String email;
}
