package com.kosa.gather_e.map_gather.vo;

import lombok.Data;

@Data
// 모집 중인 모임
// 조건 : 날짜가 지나지 않음
public class CurrentRecruitGatherVO {
	// TB_GATHER
    private long gatherSeq;
    private long categorySeq;
    private String gatherDate;
    private double gatherLatitude;
    private double gatherLongitude;
}
