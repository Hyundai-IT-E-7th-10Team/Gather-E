package com.kosa.gather_e.map_gather.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kosa.gather_e.map_gather.vo.CurrentRecruitGatherVO;

@Mapper
public interface MapGatherDAO {
	
	List<CurrentRecruitGatherVO> selectAllCurrentRecruitGather();

}
