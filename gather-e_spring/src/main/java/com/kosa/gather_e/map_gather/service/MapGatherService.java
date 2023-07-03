

package com.kosa.gather_e.map_gather.service;

import java.util.List;

import com.kosa.gather_e.gather.vo.GatherVO;
import com.kosa.gather_e.map_gather.vo.CurrentRecruitGatherVO;
import com.kosa.gather_e.map_gather.vo.PastMeetingGatherVO;

public interface MapGatherService {
	
	List<CurrentRecruitGatherVO> getAllCurrentRecruitGatherVO();
	
	List<PastMeetingGatherVO> getAllPastMeetingGatherVO(long userSeq);

}
