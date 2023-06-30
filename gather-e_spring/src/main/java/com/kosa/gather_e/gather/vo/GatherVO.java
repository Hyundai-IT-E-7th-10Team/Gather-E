package com.kosa.gather_e.gather.vo;

import lombok.Data;

import java.util.List;

@Data
public class GatherVO {
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
