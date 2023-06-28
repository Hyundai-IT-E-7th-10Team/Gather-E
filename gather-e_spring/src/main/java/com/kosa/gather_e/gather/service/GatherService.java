package com.kosa.gather_e.gather.service;

import com.kosa.gather_e.gather.dao.GatherDAO;
import com.kosa.gather_e.gather.vo.GatherImgVO;
import com.kosa.gather_e.gather.vo.GatherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatherService implements GatherServiceImpl{

    private final GatherDAO gatherDAO;

    public GatherVO createGather(GatherVO gatherVO){
        gatherDAO.insertGatherVO(gatherVO);
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
