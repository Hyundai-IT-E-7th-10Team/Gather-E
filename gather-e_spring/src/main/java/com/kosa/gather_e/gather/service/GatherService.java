package com.kosa.gather_e.gather.service;

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
public class GatherService implements GatherServiceImpl{

    private final GatherDAO gatherDAO;
    private final UserGatherDAO userGatherDAO;

    @Transactional
    public GatherVO createGather(GatherVO gatherVO){
        gatherVO.setGatherCreator(SecurityUtil.getCurrentMemberSeq());
        System.out.println(gatherVO);
        gatherDAO.insertGatherVO(gatherVO);
        UserGatherVO userGatherVO = new UserGatherVO();
        userGatherVO.setGatherSeq(gatherVO.getGatherSeq());
        userGatherVO.setUserSeq(gatherVO.getGatherCreator());
        userGatherDAO.insertUserGather(userGatherVO);
        return gatherVO;
    }

    public List<GatherVO> getAllGather() {
        return gatherDAO.selectAllGather();
    }

    public GatherVO getOneGather(int gatherSeq) {
        return gatherDAO.selectOneGatherByGatherSeq(gatherSeq);
    }

    public String deleteGather(int gatherSeq) {
        gatherDAO.deleteGatherByGatherSeq(gatherSeq);
        return "";
    }

    public GatherVO updateGather(GatherVO gatherVO) {
        gatherDAO.updateGather(gatherVO);
        return gatherVO;
    }

}
