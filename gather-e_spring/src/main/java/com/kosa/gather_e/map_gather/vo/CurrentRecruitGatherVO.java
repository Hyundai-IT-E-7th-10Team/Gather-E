package com.kosa.gather_e.map_gather.vo;

import lombok.Data;

@Data
// 모집 중인 모임
// 조건 : 날짜가 지나지 않은걸 가져오고, 
public class CurrentRecruitGatherVO {
	// TB_GATHER
    private long gatherSeq;
    private long categorySeq;
    private long gatherCreator;
    private String gatherTitle;
    private String gatherDate;
    private String gatherDescription;
    private int gatherLimit;
    private double gatherLatitude;
    private double gatherLongitude;
    private String gatherLocationName;
    
    private int cnt; 
}
