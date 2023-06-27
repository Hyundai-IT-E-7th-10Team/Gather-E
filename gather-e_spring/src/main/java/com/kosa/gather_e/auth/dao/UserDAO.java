package com.kosa.gather_e.auth.dao;

import com.kosa.gather_e.auth.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {

    UserVO selectUserByEmail(String userEmail);
    void insertUser(UserVO user);
    void updateUser(UserVO user);
}
