package com.kosa.gather_e.gather.service;

import com.kosa.gather_e.gather.dao.GatherDAO;
import com.kosa.gather_e.gather.vo.GatherImgVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatherImgService implements GatherImgServiceImpl {

    private final GatherDAO gatherDAO;

    public List<GatherImgVO> getGatherImage(int gatherSeq) {
        return gatherDAO.selectAllGatherImageByGatherSeq(gatherSeq);
    }

    public GatherImgVO addGatherImage(GatherImgVO gatherImgVO) {
        gatherDAO.insertGatherImg(gatherImgVO);
        return gatherImgVO;
    }

    public String deleteGatherImage(int gatherSeq) {
        gatherDAO.deleteGatherImageByGatherSeq(gatherSeq);
        return "";
    }
}
