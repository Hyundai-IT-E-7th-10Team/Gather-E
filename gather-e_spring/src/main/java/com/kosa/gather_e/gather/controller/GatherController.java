package com.kosa.gather_e.gather.controller;


import com.kosa.gather_e.gather.service.GatherImgService;
import com.kosa.gather_e.gather.service.GatherService;
import com.kosa.gather_e.gather.vo.GatherImgVO;
import com.kosa.gather_e.gather.vo.GatherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GatherController {

    private final GatherService gatherService;

    private final GatherImgService gatherImgService;

    /**
     * 모임생성 api
     * @param gatherVO 모임객체
     * @return ResponseEntity<GatherVO>
     */
    @PostMapping("/gather")
    public ResponseEntity<GatherVO> createGather(@RequestBody GatherVO gatherVO){
        return new ResponseEntity<>(gatherService.createGather(gatherVO), HttpStatus.CREATED);
    }

    /**
     * 전체 모임 조회 api
     * @return ResponseEntity<List<GatherVO>>
     */
    @GetMapping("/gather")
    public ResponseEntity<List<GatherVO>> getAllGather(){
        return new ResponseEntity<>(gatherService.getAllGather(), HttpStatus.OK);
    }

    /**
     * 개별 모임 조회 api
     * @param gatherSeq
     * @return ResponseEntity<GatherVO>
     */
    @GetMapping("/gather/{gatherSeq}")
    public ResponseEntity<GatherVO> getOneGather(@PathVariable int gatherSeq){
        return new ResponseEntity<>(gatherService.getOneGather(gatherSeq), HttpStatus.OK);
    }

    /**
     * 개별 모임 이미지 목록 조회
     * @param gatherSeq
     * @return ResponseEntity<List<GatherImgVO>>
     */
    @GetMapping("/gather/{gatherSeq}/image")
    public ResponseEntity<List<GatherImgVO>> getGatherImage(@PathVariable int gatherSeq){
        return new ResponseEntity<>(gatherImgService.getGatherImage(gatherSeq), HttpStatus.OK);
    }

    /**
     * 개별 모임 이미지 추가
     * @param gatherSeq
     * @param gatherImgVO
     * @return ResponseEntity<GatherImgVO>
     */
    @PostMapping("/gather/{gatherSeq}/image")
    public ResponseEntity<GatherImgVO> addGatherImage(@PathVariable int gatherSeq, @RequestBody GatherImgVO gatherImgVO){
        gatherImgVO.setGatherSeq(gatherSeq);
        return new ResponseEntity<>(gatherImgService.addGatherImage(gatherImgVO), HttpStatus.OK);
    }

    /**
     * 개별 모임 이미지 삭제
     * @param gatherSeq
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/gather/{gatherSeq}/image")
    public ResponseEntity<String> deleteGatherImage(@PathVariable int gatherSeq){
        return new ResponseEntity<>(gatherImgService.deleteGatherImage(gatherSeq), HttpStatus.OK);
    }

    /**
     * 개별 모임 삭제
     * @param gatherSeq
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/gather/{gatherSeq}")
    public ResponseEntity<String> deleteGather(@PathVariable int gatherSeq){
        return new ResponseEntity<>(gatherService.deleteGather(gatherSeq),HttpStatus.OK);
    }

    /**
     * 개별 모임 수정
     * @param gatherVO
     * @param gatherSeq
     * @return ResponseEntity<GatherVO>
     */
    @PutMapping("/gather/{gatherSeq}")
    public ResponseEntity<GatherVO> updateOneGather(@RequestBody GatherVO gatherVO,  @PathVariable int gatherSeq){
        gatherVO.setGatherSeq(gatherSeq);
        return new ResponseEntity<>(gatherService.updateGather(gatherVO), HttpStatus.OK);
    }
}
