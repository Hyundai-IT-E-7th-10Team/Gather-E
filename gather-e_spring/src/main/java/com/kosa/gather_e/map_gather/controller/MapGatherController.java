package com.kosa.gather_e.map_gather.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosa.gather_e.map_gather.service.MapGatherService;
import com.kosa.gather_e.map_gather.vo.CurrentRecruitGatherVO;

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
	
}
