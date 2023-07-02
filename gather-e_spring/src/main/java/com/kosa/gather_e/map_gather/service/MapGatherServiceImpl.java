package com.kosa.gather_e.map_gather.service;

import java.util.List;

import com.kosa.gather_e.gather.vo.GatherVO;
import com.kosa.gather_e.map_gather.vo.CurrentRecruitGatherVO;

public interface MapGatherServiceImpl {
	
	List<CurrentRecruitGatherVO> getAllCurrentRecruitGatherVO();

}
