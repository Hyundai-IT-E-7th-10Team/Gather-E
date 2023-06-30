package com.kosa.gather_e.user_gather.service;

import com.kosa.gather_e.auth.vo.UserVO;
import com.kosa.gather_e.user_gather.vo.UserGatherVO;

import java.util.List;

public interface UserGatherService {

    List<UserVO> getUsersInGather(long gatherSeq);

    void joinGather(UserGatherVO userGatherVO);

    void leaveGather(UserGatherVO userGatherVO);
}
