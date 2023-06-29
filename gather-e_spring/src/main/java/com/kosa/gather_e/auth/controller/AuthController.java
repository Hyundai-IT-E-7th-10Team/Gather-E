package com.kosa.gather_e.auth.controller;

import com.kosa.gather_e.auth.dto.Token;
import com.kosa.gather_e.auth.service.KakaoAuthService;
import com.kosa.gather_e.auth.vo.UserVO;
import com.kosa.gather_e.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenUtils jwtTokenUtils;
    private final KakaoAuthService kakaoAuthService;

    /**
     * KAKAO 소셜 로그인
     * @return ResponseEntity<AuthResource>
     */
    @GetMapping("/kakao")
    public ResponseEntity<Token> login(@RequestParam String accessToken) {
        UserVO user = kakaoAuthService.login(accessToken);
        String token = jwtTokenUtils.generateJwtToken(user);
        return new ResponseEntity<>(new Token(token), HttpStatus.OK);
    }
}
