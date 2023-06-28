package com.kosa.gather_e.gather.vo;

import lombok.Data;

import java.util.List;

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
//    private List<GatherImgVO> gatherImages;
}
