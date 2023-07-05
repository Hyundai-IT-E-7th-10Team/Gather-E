package com.kosa.gather_e.auth.controller;

import com.kosa.gather_e.auth.service.Provider;
import com.kosa.gather_e.auth.service.UserService;
import com.kosa.gather_e.auth.vo.LoginResponse;
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
    private final UserService userService;
    /**
     * KAKAO 소셜 로그인
     * @return ResponseEntity<AuthResource>
     */
    @GetMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String kakaoToken) throws Exception {
        UserVO user = userService.login(kakaoToken, Provider.KAKAO);
        String token = jwtTokenUtils.generateJwtToken(user);
        return new ResponseEntity<>(new LoginResponse(user, token), HttpStatus.OK);
    }

    @GetMapping("/google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestParam String googleToken) throws Exception {
        UserVO user = userService.login(googleToken, Provider.GOOGLE);
        String token = jwtTokenUtils.generateJwtToken(user);
        return new ResponseEntity<>(new LoginResponse(user, token), HttpStatus.OK);
    }
}
