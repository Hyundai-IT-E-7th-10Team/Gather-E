package com.kosa.gather_e.map_gather.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kosa.gather_e.map_gather.dao.MapGatherDAO;
import com.kosa.gather_e.map_gather.vo.CurrentRecruitGatherVO;
import com.kosa.gather_e.map_gather.vo.PastMeetingGatherVO;
import com.kosa.gather_e.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapGatherServiceImpl implements MapGatherService {
	
	private final MapGatherDAO mapGatherDAO;
	
	public List<CurrentRecruitGatherVO> getAllCurrentRecruitGatherVO() {
		return mapGatherDAO.selectAllCurrentRecruitGather();
	}
	

	public List<PastMeetingGatherVO> getAllPastMeetingGatherVO(long userSeq) {
		return mapGatherDAO.selectAllPastMeetingGather(userSeq);
	}

}