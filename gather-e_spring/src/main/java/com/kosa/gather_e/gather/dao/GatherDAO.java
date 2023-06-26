package com.kosa.gather_e.gather.dao;

import com.kosa.gather_e.gather.vo.GatherVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GatherDAO {

    int insertGatherVO(GatherVO gatherVO);

    List<GatherVO> selectAllGather();

    GatherVO selectOneGather(int gatherSeq);

    void deleteGather(int gatherSeq);

    int updateGather(GatherVO gatherVO);
}
