package com.kosa.gather_e.gather.vo;

import lombok.Data;

@Data
public class GatherVO {
    private int gatherSeq;
    private int categorySeq;
    private String gatherTitle;
    private String gatherDate;
    private String gatherDescription;
    private int gatherLimit;
    private double gatherLatitude;
    private double gatherLongitude;
}
