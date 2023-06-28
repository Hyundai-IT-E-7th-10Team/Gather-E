package com.kosa.gather_e.auth.service;

import com.kosa.gather_e.auth.dto.KakaoUserResponse;
import com.kosa.gather_e.auth.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoClient {

    public UserVO getUser(String accessToken) throws NullPointerException {
        WebClient webClient = WebClient.create();
        KakaoUserResponse kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();

        return kakaoUserResponse.toUserVO();
    }


}
