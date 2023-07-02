package com.kosa.gather_e.auth.service;

import com.kosa.gather_e.auth.dao.UserDAO;
import com.kosa.gather_e.auth.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserDAO userDAO;
    private final KakaoClient kakaoClient;

    public UserVO login(String token) {
        UserVO user = kakaoClient.getUser(token);
        UserVO selectedUser = userDAO.selectUserByEmail(user.getUserEmail());
        if(selectedUser == null) {//첫 로그인
            userDAO.insertUser(user);
        } else {//가입이 되어있는 경우
            userDAO.updateUser(user);
            user.setUserSeq(selectedUser.getUserSeq());
        }
        return user;
    };
}
