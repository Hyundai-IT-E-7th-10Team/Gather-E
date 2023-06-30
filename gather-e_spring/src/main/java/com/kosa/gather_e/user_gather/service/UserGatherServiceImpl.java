package com.kosa.gather_e.user_gather.service;

import com.kosa.gather_e.auth.vo.UserVO;
import com.kosa.gather_e.gather.dao.GatherDAO;
import com.kosa.gather_e.gather.vo.GatherVO;
import com.kosa.gather_e.user_gather.dao.UserGatherDAO;
import com.kosa.gather_e.user_gather.vo.UserGatherVO;
import com.kosa.gather_e.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGatherServiceImpl implements UserGatherService {

    private final GatherDAO gatherDAO;
    private final UserGatherDAO userGatherDAO;

    @Override
    public List<UserVO> getUsersInGather(long gatherSeq) {
        return userGatherDAO.selectAllUserByGatherSeq(gatherSeq);
    }

    @Override
    public void joinGather(UserGatherVO userGatherVO) {
        userGatherVO.setUserSeq(SecurityUtil.getCurrentMemberSeq());
        userGatherDAO.insertUserGather(userGatherVO);
    }

    @Override
    @Transactional
    public void leaveGather(UserGatherVO userGatherVO) {
        GatherVO gather = gatherDAO.selectOneGatherByGatherSeq(userGatherVO.getGatherSeq());
        long currUser = SecurityUtil.getCurrentMemberSeq();
        if(gather.getGatherCreator() == currUser) {
            gatherDAO.deleteGatherByGatherSeq(gather.getGatherSeq());
        }
        userGatherVO.setUserSeq(currUser);
        userGatherDAO.deleteUserGather(userGatherVO);
    }
}
