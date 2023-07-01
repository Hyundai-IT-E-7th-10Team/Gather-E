package com.kosa.gather_e.map_gather.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kosa.gather_e.map_gather.dao.MapGatherDAO;
import com.kosa.gather_e.map_gather.vo.CurrentRecruitGatherVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapGatherService implements MapGatherServiceImpl {
	
	private final MapGatherDAO mapGatherDAO;
	
	public List<CurrentRecruitGatherVO> getAllCurrentRecruitGatherVO() {
		return mapGatherDAO.selectAllCurrentRecruitGather();
	}

}
