package com.kosa.gather_e.gather.service;

import com.kosa.gather_e.gather.vo.GatherImgVO;
import com.kosa.gather_e.gather.vo.GatherVO;

import java.util.List;

public interface GatherServiceImpl {

    GatherVO createGather(GatherVO gatherVO);

    List<GatherVO> getAllGather();

    GatherVO getOneGather(int gatherSeq);

    String deleteGather(int gatherSeq);

    GatherVO updateGather(GatherVO gatherVO);
}
