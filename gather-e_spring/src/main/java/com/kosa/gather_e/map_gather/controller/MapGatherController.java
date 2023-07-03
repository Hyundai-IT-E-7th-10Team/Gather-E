package com.kosa.gather_e.map_gather.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosa.gather_e.map_gather.service.MapGatherService;
import com.kosa.gather_e.map_gather.vo.CurrentRecruitGatherVO;
import com.kosa.gather_e.map_gather.vo.PastMeetingGatherVO;
import com.kosa.gather_e.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MapGatherController {

	private final MapGatherService mapGatherService;
	
	/** 현재 모집 중인 모임 조회 api
    * @return ResponseEntity<List<CurrentRecruitGatherVO>>
    */
   @GetMapping("/map/currentrecruit")
   public ResponseEntity<List<CurrentRecruitGatherVO>> getAllCurrentRecruitGather(){
       return new ResponseEntity<>(mapGatherService.getAllCurrentRecruitGatherVO(), HttpStatus.OK);
   }
   
	/** 내가 참여했던 모임 조회 api
    * @return ResponseEntity<List<PastMeetingGatherVO>>
    */
   @GetMapping("/map/past-meeting")
   public ResponseEntity<List<PastMeetingGatherVO>> getAllPastMeetingGather() {
	   return new ResponseEntity<List<PastMeetingGatherVO>>(
			   mapGatherService.getAllPastMeetingGatherVO(SecurityUtil.getCurrentMemberSeq()),HttpStatus.OK);
   }
}
