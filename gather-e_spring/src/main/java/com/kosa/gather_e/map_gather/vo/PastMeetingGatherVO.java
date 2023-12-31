package com.kosa.gather_e.map_gather.vo;

import lombok.Data;

@Data
public class PastMeetingGatherVO {
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
}
