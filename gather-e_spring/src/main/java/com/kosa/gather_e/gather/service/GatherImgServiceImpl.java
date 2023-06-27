package com.kosa.gather_e.gather.service;

import com.kosa.gather_e.gather.vo.GatherImgVO;

import java.util.List;

public interface GatherImgServiceImpl {


    List<GatherImgVO> getGatherImage(int gatherSeq);


    GatherImgVO addGatherImage(GatherImgVO gatherImgVO);


    String deleteGatherImage(int gatherSeq);

}
