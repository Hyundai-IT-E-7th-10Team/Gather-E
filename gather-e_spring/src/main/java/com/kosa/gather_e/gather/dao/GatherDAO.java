package com.kosa.gather_e.gather.dao;

import com.kosa.gather_e.gather.vo.GatherImgVO;
import com.kosa.gather_e.gather.vo.GatherVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GatherDAO {

    int insertGatherVO(GatherVO gatherVO);

//    void insertGatherImgVO(int gatherSeq, List<GatherImgVO> gatherImages);

    List<GatherVO> selectAllGather();

    GatherVO selectOneGatherByGatherSeq(int gatherSeq);

    void deleteGatherByGatherSeq(int gatherSeq);

    int updateGather(GatherVO gatherVO);

    List<GatherImgVO> selectAllGatherImageByGatherSeq(int gatherSeq);

    void insertGatherImg(GatherImgVO gatherImgVO);

    void deleteGatherImageByGatherSeq(int gatherSeq);
}
