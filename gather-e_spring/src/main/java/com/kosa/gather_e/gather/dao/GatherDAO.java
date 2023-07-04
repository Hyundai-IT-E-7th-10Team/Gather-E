package com.kosa.gather_e.gather.dao;

import com.kosa.gather_e.gather.vo.GatherImgVO;
import com.kosa.gather_e.gather.vo.GatherVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GatherDAO {

    long insertGatherVO(GatherVO gatherVO);

    List<GatherVO> selectAllGather();

    GatherVO selectOneGatherByGatherSeq(long gatherSeq);

    void deleteGatherByGatherSeq(long gatherSeq);

    void updateGather(GatherVO gatherVO);

    List<GatherImgVO> selectAllGatherImageByGatherSeq(long gatherSeq);

    void insertGatherImg(GatherImgVO gatherImgVO);

    void deleteGatherImageByGatherSeq(long gatherSeq);

    List<GatherVO> selectAllGatherByCategorySeq(long categorySeq);
}
