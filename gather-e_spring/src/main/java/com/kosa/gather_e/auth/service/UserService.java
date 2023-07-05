package com.kosa.gather_e.auth.service;

import com.kosa.gather_e.auth.dao.UserDAO;
import com.kosa.gather_e.auth.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final GoogleAuthService googleAuthService;
    private final KakaoAuthService kakaoAuthService;

    public UserVO login(String token, Provider provider) throws Exception {
        UserVO user = loadUser(token, provider);
        UserVO selectedUser = userDAO.selectUserByEmail(user.getUserEmail());
        if (selectedUser == null) {//첫 로그인
            userDAO.insertUser(user);
        } else {//가입이 되어있는 경우
            userDAO.updateUser(user);
            user.setUserSeq(selectedUser.getUserSeq());
        }
        return user;
    }

    private UserVO loadUser(String token, Provider provider) throws Exception {
        switch (provider) {
            case KAKAO:
                return kakaoAuthService.getUser(token);
            case GOOGLE:
                return googleAuthService.getUser(token);
        }
        throw new Exception();
    }
}
